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
import pl.edu.umg.linkumg.dtos.RefreshTokenDto;
import pl.edu.umg.linkumg.dtos.TokenDto;
import pl.edu.umg.linkumg.dtos.register.CollegeRegisterDto;
import pl.edu.umg.linkumg.dtos.register.CompanyRegisterDto;
import pl.edu.umg.linkumg.dtos.register.StudentRegisterDto;
import pl.edu.umg.linkumg.exceptions.*;
import pl.edu.umg.linkumg.models.ActivationToken;
import pl.edu.umg.linkumg.models.RefreshToken;
import pl.edu.umg.linkumg.models.users.College;
import pl.edu.umg.linkumg.models.users.Company;
import pl.edu.umg.linkumg.models.users.Student;
import pl.edu.umg.linkumg.models.users.User;
import pl.edu.umg.linkumg.repositories.ActivationTokenRepository;
import pl.edu.umg.linkumg.repositories.RefreshTokenRepository;
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

        generateActivationTokenAndSendIt(student);
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

        generateActivationTokenAndSendIt(company);
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

        generateActivationTokenAndSendIt(college);
    }

    @Transactional
    public TokenDto generateTokenByCredentials(LoginDto loginDto) throws UserIsNotActivatedException, UserBadCredentialsException, UserIsBannedException, ResourceNotFoundException {
        User user = userRepository
                .findByEmail(loginDto.email())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                String.format("User with email %s not found.", loginDto.email())
                        ));
        if (!passwordEncoder.matches(loginDto.password(), user.getPassword()))
            throw new UserBadCredentialsException("Bad credentials.");

        return new TokenDto(
                generateAccessToken(user),
                generateRefreshToken(user)
        );
    }

    @Transactional
    public TokenDto renewToken(RefreshTokenDto refreshTokenDto) throws UserIsBannedException, UserIsNotActivatedException, ResourceNotFoundException, TokenExpiredException {
        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(refreshTokenDto.token())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                String.format("Token %s not found.", refreshTokenDto.token())
                        ));

        refreshToken.checkExpired();

        return new TokenDto(
                generateAccessToken(refreshToken.getUser()),
                refreshToken.getToken()
        );
    }

    // TODO: invalid token
    public void logout() {
    }

    public Object findUserById(String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        User user = findUserFromDBById(stringUUID);

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
    public void remindPassword(String email) {
        userRepository.findByEmail(email);
        // TODO: SENDGRID remindPassword
    }

    @Transactional
    public void banUser(String stringUUID) throws UUIDConversionException, ResourceNotFoundException {
        User user = findUserFromDBById(stringUUID);
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

    private User findUserFromDBById(String stringUUID) throws UUIDConversionException, ResourceNotFoundException {
        return userRepository.findById(
                ObjectDtoConverter.stringUUIDToUUID(stringUUID)
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        String.format("User with id %s not found.", stringUUID)
                )
        );
    }

    private void generateActivationTokenAndSendIt(User user){
        // TODO: 13/11/2022 activation email
        ActivationToken activationToken = new ActivationToken(user);
        activationTokenRepository.save(activationToken);

        System.out.println(String.format("http://localhost:8080/users/activate?token=%s", activationToken.getToken()));
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
