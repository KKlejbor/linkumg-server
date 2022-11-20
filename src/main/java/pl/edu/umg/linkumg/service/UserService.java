package pl.edu.umg.linkumg.service;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.umg.linkumg.dtos.LoginDto;
import pl.edu.umg.linkumg.dtos.PasswordChangeDto;
import pl.edu.umg.linkumg.dtos.TokenDto;
import pl.edu.umg.linkumg.dtos.users.CollegeDto;
import pl.edu.umg.linkumg.dtos.users.CompanyDto;
import pl.edu.umg.linkumg.dtos.users.StudentDto;
import pl.edu.umg.linkumg.dtos.users.register.CollegeRegisterDto;
import pl.edu.umg.linkumg.dtos.users.register.CompanyRegisterDto;
import pl.edu.umg.linkumg.dtos.users.register.StudentRegisterDto;
import pl.edu.umg.linkumg.dtos.users.update.CollegeUpdateDto;
import pl.edu.umg.linkumg.dtos.users.update.CompanyUpdateDto;
import pl.edu.umg.linkumg.dtos.users.update.StudentUpdateDto;
import pl.edu.umg.linkumg.exceptions.*;
import pl.edu.umg.linkumg.models.tokens.ActivationToken;
import pl.edu.umg.linkumg.models.tokens.RefreshToken;
import pl.edu.umg.linkumg.models.tokens.RemindPasswordToken;
import pl.edu.umg.linkumg.models.users.College;
import pl.edu.umg.linkumg.models.users.Company;
import pl.edu.umg.linkumg.models.users.Student;
import pl.edu.umg.linkumg.models.users.User;
import pl.edu.umg.linkumg.repositories.ActivationTokenRepository;
import pl.edu.umg.linkumg.repositories.RefreshTokenRepository;
import pl.edu.umg.linkumg.repositories.RemindPasswordRepository;
import pl.edu.umg.linkumg.repositories.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Service
@AllArgsConstructor
public class UserService {

    private final JwtEncoder encoder;
    private final PasswordEncoder passwordEncoder;

    private UserRepository userRepository;
    private ActivationTokenRepository activationTokenRepository;
    private RefreshTokenRepository refreshTokenRepository;
    private RemindPasswordRepository remindPasswordRepository;
    private SendGridService sendGridService;

    @Transactional
    public void registerStudent(StudentRegisterDto studentRegisterDto) throws ResourceViolationException {
        Student student = new Student(
                studentRegisterDto.email(),
                passwordEncoder.encode(studentRegisterDto.password()),
                studentRegisterDto.name(),
                studentRegisterDto.lastName(),
                studentRegisterDto.indexNumber(),
                studentRegisterDto.faculty()
        );

        student.setCreatedAt(LocalDateTime.now());
        student.setLastUpdatedAt(LocalDateTime.now());

        saveNewInDB(student);

        generateActivationTokenAndSend(student);
    }

    @Transactional
    public void registerCompany(CompanyRegisterDto companyRegisterDto) throws ResourceViolationException {
        Company company = new Company(
                companyRegisterDto.email(),
                passwordEncoder.encode(companyRegisterDto.password()),
                companyRegisterDto.name(),
                companyRegisterDto.nip()
        );

        company.setCreatedAt(LocalDateTime.now());
        company.setLastUpdatedAt(LocalDateTime.now());

        saveNewInDB(company);

        generateActivationTokenAndSend(company);
    }

    @Transactional
    public void registerCollege(CollegeRegisterDto collegeRegisterDto) throws ResourceViolationException {
        College college = new College(
                collegeRegisterDto.email(),
                passwordEncoder.encode(collegeRegisterDto.password()),
                collegeRegisterDto.name(),
                collegeRegisterDto.faculty()
        );

        college.setCreatedAt(LocalDateTime.now());
        college.setLastUpdatedAt(LocalDateTime.now());

        saveNewInDB(college);

        generateActivationTokenAndSend(college);
    }

    @Transactional
    public TokenDto generateTokenByCredentials(LoginDto loginDto) throws UserIsNotActivatedException, UserBadCredentialsException, UserIsBannedException, ResourceNotFoundException {
        User user = checkUserExistsByEmail(loginDto.email());
        if (!passwordEncoder.matches(loginDto.password(), user.getPassword()))
            throw new UserBadCredentialsException("Bad credentials.");

        return new TokenDto(
                generateAccessToken(user),
                generateRefreshToken(user)
        );
    }

    @Transactional
    public TokenDto renewToken(String token) throws UserIsBannedException, UserIsNotActivatedException, ResourceNotFoundException, TokenExpiredException {
        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                String.format("Token %s not found.", token)
                        ));

        refreshToken.checkExpired();

        return new TokenDto(
                generateAccessToken(refreshToken.getUser()),
                refreshToken.getToken()
        );
    }

    @Transactional
    public void remindPassword(String email) throws ResourceNotFoundException {
        User user = checkUserExistsByEmail(email);

        RemindPasswordToken remindPasswordToken = new RemindPasswordToken(user);
        remindPasswordRepository.save(remindPasswordToken);

        sendGridService.sendEmail(remindPasswordToken);
    }

    // TODO: invalid token
    public void logout() {
    }

    public Object findUserById(String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        User user = checkUserExistsById(stringUUID);

        if (user instanceof Student student) {
            return ObjectDtoConverter.studentObjectToStudentDto(student);
        } else if (user instanceof Company company) {
            return ObjectDtoConverter.companyObjectToCompanyDto(company);
        } else {
            College college = (College) user;
            return ObjectDtoConverter.collegeObjectToCollegeDto(college);
        }
    }

    // TODO: findUsersPage
    public Page<User> findUsersPage(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional
    public void activate(String token) throws ResourceNotFoundException, TokenExpiredException {
        ActivationToken activationToken = activationTokenRepository.findByToken(token)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                String.format("Token %s not found.", token)
                        )
                );
        activationToken.checkExpired();

        User user = activationToken.getUser();
        user.setActive(true);

        userRepository.save(user);
        activationTokenRepository.deleteById(activationToken.getId());
    }

    @Transactional
    public void changePassword(PasswordChangeDto passwordChangeDto, String email) throws ResourceNotFoundException {
        User user = checkUserExistsByEmail(email);

        user.setPassword(
                passwordEncoder.encode(passwordChangeDto.password())
        );

        userRepository.save(user);
    }

    @Transactional
    public void changePasswordByRemindToken(PasswordChangeDto passwordChangeDto, String token) throws ResourceNotFoundException, TokenExpiredException {
        RemindPasswordToken remindPasswordToken = remindPasswordRepository.findByToken(token)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                String.format("Token %s not found.", token)
                        )
                );
        remindPasswordToken.checkExpired();

        User user = remindPasswordToken.getUser();
        user.setPassword(
                passwordEncoder.encode(passwordChangeDto.password())
        );

        userRepository.save(user);
        remindPasswordRepository.deleteById(remindPasswordToken.getId());
    }

    @Transactional
    public StudentDto updateStudent(StudentUpdateDto studentUpdateDto, String email) throws ResourceNotFoundException {
        User user = checkUserExistsByEmail(email);

        Student student = (Student) user;

        // TODO: validation

        if (studentUpdateDto.birthDate() != null) {
            student.setBirthYear(studentUpdateDto.birthDate());
        }
        if (studentUpdateDto.phone() != null || !studentUpdateDto.phone().isBlank()) {
            student.setPhoneNumber(studentUpdateDto.phone());
        }
        if (studentUpdateDto.about() != null || !studentUpdateDto.about().isBlank()) {
            student.setDescription(studentUpdateDto.about());
        }
        if (studentUpdateDto.socials().github() != null || !studentUpdateDto.socials().github().isBlank()) {
            student.getSocials().setGithub(studentUpdateDto.socials().github());
        }
        if (studentUpdateDto.socials().linkedin() != null || !studentUpdateDto.socials().linkedin().isBlank()) {
            student.getSocials().setLinkedin(studentUpdateDto.socials().linkedin());
        }
        if (studentUpdateDto.socials().facebook() != null || !studentUpdateDto.socials().facebook().isBlank()) {
            student.getSocials().setFacebook(studentUpdateDto.socials().facebook());
        }
        if (studentUpdateDto.socials().instagram() != null || !studentUpdateDto.socials().instagram().isBlank()) {
            student.getSocials().setInstagram(studentUpdateDto.socials().instagram());
        }
        if (studentUpdateDto.socials().ownSite() != null || !studentUpdateDto.socials().ownSite().isBlank()) {
            student.getSocials().setOwnSite(studentUpdateDto.socials().ownSite());
        }

        // TODO: avatar

        userRepository.save(student);

        return ObjectDtoConverter.studentObjectToStudentDto(student);
    }

    @Transactional
    public CompanyDto updateCompany(CompanyUpdateDto companyUpdateDto, String email) throws ResourceNotFoundException {
        User user = checkUserExistsByEmail(email);

        Company company = (Company) user;

        // TODO: validation

        if (companyUpdateDto.phone() != null || !companyUpdateDto.phone().isBlank()) {
            company.setPhoneNumber(companyUpdateDto.phone());
        }
        if (companyUpdateDto.about() != null || !companyUpdateDto.about().isBlank()) {
            company.setDescription(companyUpdateDto.about());
        }
        if (companyUpdateDto.socials().github() != null || !companyUpdateDto.socials().github().isBlank()) {
            company.getSocials().setGithub(companyUpdateDto.socials().github());
        }
        if (companyUpdateDto.socials().linkedin() != null || !companyUpdateDto.socials().linkedin().isBlank()) {
            company.getSocials().setLinkedin(companyUpdateDto.socials().linkedin());
        }
        if (companyUpdateDto.socials().facebook() != null || !companyUpdateDto.socials().facebook().isBlank()) {
            company.getSocials().setFacebook(companyUpdateDto.socials().facebook());
        }
        if (companyUpdateDto.socials().instagram() != null || !companyUpdateDto.socials().instagram().isBlank()) {
            company.getSocials().setInstagram(companyUpdateDto.socials().instagram());
        }
        if (companyUpdateDto.socials().ownSite() != null || !companyUpdateDto.socials().ownSite().isBlank()) {
            company.getSocials().setOwnSite(companyUpdateDto.socials().ownSite());
        }

        // TODO: avatar

        userRepository.save(company);

        return ObjectDtoConverter.companyObjectToCompanyDto(company);
    }

    @Transactional
    public CollegeDto updateCollege(CollegeUpdateDto collegeUpdateDto, String email) throws ResourceNotFoundException {
        User user = checkUserExistsByEmail(email);

        College college = (College) user;

        // TODO: validation

        if (collegeUpdateDto.about() != null || !collegeUpdateDto.about().isBlank()) {
            college.setDescription(collegeUpdateDto.about());
        }

        // TODO: avatar

        userRepository.save(college);

        return ObjectDtoConverter.collegeObjectToCollegeDto(college);
    }

    @Transactional
    public void banUser(String stringUUID) throws UUIDConversionException, ResourceNotFoundException {
        User user = checkUserExistsById(stringUUID);
        user.setBanned(true);
        userRepository.save(user);
    }

    private void saveNewInDB(User user) throws ResourceViolationException {
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceViolationException(
                    String.format("Email %s already exists.", user.getEmail())
            );
        }
    }

    private User checkUserExistsById(String stringUUID) throws UUIDConversionException, ResourceNotFoundException {
        return userRepository
                .findById(ObjectDtoConverter.stringUUIDToUUID(stringUUID))
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                String.format("User with id %s not found.", stringUUID)
                        )
                );
    }

    private User checkUserExistsByEmail(String email) throws ResourceNotFoundException {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                String.format("User with email %s not found.", email)
                        ));
    }

    private void generateActivationTokenAndSend(User user) {
        ActivationToken activationToken = new ActivationToken(user);
        activationTokenRepository.save(activationToken);

        sendGridService.sendEmail(activationToken);
    }

    private String generateAccessToken(User user) throws UserIsNotActivatedException, UserIsBannedException {
        if (!user.isActive()) {
            throw new UserIsNotActivatedException(String.format("User %s isn't activated.", user.getEmail()));
        }
        if (user.isBanned()) {
            throw new UserIsBannedException(String.format("User %s is banned.", user.getEmail()));
        }

        Instant now = Instant.now();
        String role = user
                .getClass()
                .getSimpleName()
                .toUpperCase(Locale.ROOT);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(user.getEmail())
                .claim("scope", role)
                .build();

        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private String generateRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken(user);
        refreshTokenRepository.save(refreshToken);

        return refreshToken.getToken();
    }
}
