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
import pl.edu.umg.linkumg.validators.users.CollegeValidator;
import pl.edu.umg.linkumg.validators.users.CompanyValidator;
import pl.edu.umg.linkumg.validators.users.StudentValidator;
import pl.edu.umg.linkumg.validators.users.UserValidator;

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
    private UserValidator userValidator;
    private StudentValidator studentValidator;
    private CompanyValidator companyValidator;
    private CollegeValidator collegeValidator;

    @Transactional
    public void registerStudent(StudentRegisterDto studentRegisterDto) throws ResourceViolationException, ValidationException {

        studentValidator.validateStudentCreate(studentRegisterDto);

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
    public void registerCompany(CompanyRegisterDto companyRegisterDto) throws ResourceViolationException, ValidationException {

        companyValidator.validateCompanyCreate(companyRegisterDto);

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
    public void registerCollege(CollegeRegisterDto collegeRegisterDto) throws ResourceViolationException, ValidationException {

        collegeValidator.validateCollegeCreate(collegeRegisterDto);

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
    public void changePassword(PasswordChangeDto passwordChangeDto, String email) throws ResourceNotFoundException, ValidationException {
        User user = checkUserExistsByEmail(email);

        userValidator.validateUserPasswordChange(passwordChangeDto);

        user.setPassword(
                passwordEncoder.encode(passwordChangeDto.password())
        );

        user.setLastUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    @Transactional
    public void changePasswordByRemindToken(PasswordChangeDto passwordChangeDto, String token) throws ResourceNotFoundException, TokenExpiredException, ValidationException {
        RemindPasswordToken remindPasswordToken = remindPasswordRepository.findByToken(token)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                String.format("Token %s not found.", token)
                        )
                );
        remindPasswordToken.checkExpired();

        userValidator.validateUserPasswordChange(passwordChangeDto);

        User user = remindPasswordToken.getUser();
        user.setPassword(
                passwordEncoder.encode(passwordChangeDto.password())
        );

        user.setLastUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
        remindPasswordRepository.deleteById(remindPasswordToken.getId());
    }

    @Transactional
    public StudentDto updateStudent(StudentUpdateDto studentUpdateDto, String email) throws ResourceNotFoundException, ValidationException {
        User user = checkUserExistsByEmail(email);

        if(!(user instanceof Student student))
            throw new ClassCastException(String.format("User with %s email is not %s instance.", email, Student.class.getSimpleName()));

        int changes = 0;

        if (studentUpdateDto.birthDate() != null) {
            // TODO: checkIfNeedsValidation
            student.setBirthYear(studentUpdateDto.birthDate());
            changes++;
        }
        if (studentUpdateDto.phone() != null) {
            studentValidator.validatePhone(studentUpdateDto.phone());
            student.setPhoneNumber(studentUpdateDto.phone());
            changes++;
        }
        if (studentUpdateDto.about() != null) {
            studentValidator.validateDescription(studentUpdateDto.about());
            student.setDescription(studentUpdateDto.about());
            changes++;
        }
        if (studentUpdateDto.socials().github() != null) {
            studentValidator.validateGithub(studentUpdateDto.socials().github());
            student.getSocials().setGithub(studentUpdateDto.socials().github());
            changes++;
        }
        if (studentUpdateDto.socials().linkedin() != null) {
            studentValidator.validateLinkedin(studentUpdateDto.socials().linkedin());
            student.getSocials().setLinkedin(studentUpdateDto.socials().linkedin());
            changes++;
        }
        if (studentUpdateDto.socials().facebook() != null) {
            studentValidator.validateFacebook(studentUpdateDto.socials().facebook());
            student.getSocials().setFacebook(studentUpdateDto.socials().facebook());
            changes++;
        }
        if (studentUpdateDto.socials().instagram() != null) {
            studentValidator.validateInstagram(studentUpdateDto.socials().instagram());
            student.getSocials().setInstagram(studentUpdateDto.socials().instagram());
            changes++;
        }
        if (studentUpdateDto.socials().ownSite() != null) {
            studentValidator.validateOwnSite(studentUpdateDto.socials().ownSite());
            student.getSocials().setOwnSite(studentUpdateDto.socials().ownSite());
            changes++;
        }
        if(studentUpdateDto.avatar()!=null){
            student.setPathToAvatar(studentUpdateDto.avatar());
            changes++;
        }

        if (changes > 0) {
            student.setLastUpdatedAt(LocalDateTime.now());
            userRepository.save(student);
        }

        return ObjectDtoConverter.studentObjectToStudentDto(student);
    }

    @Transactional
    public CompanyDto updateCompany(CompanyUpdateDto companyUpdateDto, String email) throws ResourceNotFoundException, ValidationException {
        User user = checkUserExistsByEmail(email);

        if(!(user instanceof Company company))
            throw new ClassCastException(String.format("User with %s email is not %s instance.", email, Company.class.getSimpleName()));

        int changes = 0;

        if (companyUpdateDto.phone() != null) {
            companyValidator.validatePhone(companyUpdateDto.phone());
            company.setPhoneNumber(companyUpdateDto.phone());
            changes++;
        }
        if (companyUpdateDto.about() != null) {
            companyValidator.validateDescription(companyUpdateDto.about());
            company.setDescription(companyUpdateDto.about());
            changes++;
        }
        if (companyUpdateDto.socials().github() != null) {
            companyValidator.validateGithub(companyUpdateDto.socials().github());
            company.getSocials().setGithub(companyUpdateDto.socials().github());
            changes++;
        }
        if (companyUpdateDto.socials().linkedin() != null) {
            companyValidator.validateLinkedin(companyUpdateDto.socials().linkedin());
            company.getSocials().setLinkedin(companyUpdateDto.socials().linkedin());
            changes++;
        }
        if (companyUpdateDto.socials().facebook() != null) {
            companyValidator.validateFacebook(companyUpdateDto.socials().facebook());
            company.getSocials().setFacebook(companyUpdateDto.socials().facebook());
            changes++;
        }
        if (companyUpdateDto.socials().instagram() != null) {
            companyValidator.validateInstagram(companyUpdateDto.socials().instagram());
            company.getSocials().setInstagram(companyUpdateDto.socials().instagram());
            changes++;
        }
        if (companyUpdateDto.socials().ownSite() != null) {
            companyValidator.validateOwnSite(companyUpdateDto.socials().ownSite());
            company.getSocials().setOwnSite(companyUpdateDto.socials().ownSite());
            changes++;
        }
        if(companyUpdateDto.avatar()!=null){
            company.setPathToAvatar(companyUpdateDto.avatar());
            changes++;
        }

        if(changes>0) {
            company.setLastUpdatedAt(LocalDateTime.now());
            userRepository.save(company);
        }

        return ObjectDtoConverter.companyObjectToCompanyDto(company);
    }

    @Transactional
    public CollegeDto updateCollege(CollegeUpdateDto collegeUpdateDto, String email) throws ResourceNotFoundException, ValidationException {
        User user = checkUserExistsByEmail(email);

        if(!(user instanceof College college))
            throw new ClassCastException(String.format("User with %s email is not %s instance.", email, College.class.getSimpleName()));

        int changes = 0;

        if (collegeUpdateDto.about() != null) {
            collegeValidator.validateDescription(collegeUpdateDto.about());
            college.setDescription(collegeUpdateDto.about());
            changes++;
        }
        if (collegeUpdateDto.avatar() != null) {
            college.setPathToAvatar(collegeUpdateDto.avatar());
            changes++;
        }

        if (changes > 0) {
            college.setLastUpdatedAt(LocalDateTime.now());
            userRepository.save(college);
        }

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
