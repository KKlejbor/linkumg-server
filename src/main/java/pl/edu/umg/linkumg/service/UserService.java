package pl.edu.umg.linkumg.service;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import pl.edu.umg.linkumg.dto.*;
import pl.edu.umg.linkumg.exception.*;
import pl.edu.umg.linkumg.models.College;
import pl.edu.umg.linkumg.models.Company;
import pl.edu.umg.linkumg.models.Student;
import pl.edu.umg.linkumg.models.User;
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
    }

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
    }

    // TODO:
    public void registerCollege(CollegeRegisterDto collegeRegisterDto) throws ResourceViolationException {
        College college = new College(

        );

        saveNewInDB(college);
    }

    public void remaindPassword(String email) {
        userRepository.findByEmail(email);
        // TODO: remaindPassword
    }

    // TODO: refreshToken
    public TokenDto generateToken(JwtRequest jwtRequest) throws UserIsNotActivatedException, UserBadCredentialsException, UserIsBannedException {
        User user = userRepository
                .findByEmail(jwtRequest.email())
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format("User with email %s not found.", jwtRequest.email())
                        ));
        if (!passwordEncoder.matches(jwtRequest.password(), user.getPassword()))
            throw new UserBadCredentialsException("Bad credentials.");
        if (!user.isActive()) {
            throw new UserIsNotActivatedException(String.format("User %s isn't activated.", jwtRequest.email()));
        }
        if (user.isBanned()) {
            throw new UserIsBannedException(String.format("User %s is banned.", jwtRequest.email()));
        }

        Instant now = Instant.now();
        String scope = user
                .getClass()
                .getSimpleName()
                .toUpperCase(Locale.ROOT);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.MINUTES))
                .subject(jwtRequest.email())
                .claim("scope", scope)
                .build();

        return new TokenDto(
                "bearer",
                this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue(),
                null
        );
    }

    // TODO: findUserById
    public Object findUserById(String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        User user = findUserFromDBById(stringUUID);

        if (user instanceof Student student) {
            return new StudentDto(
                    student.getClass().getSimpleName().toLowerCase(),
                    student.getUserId(),
                    student.getName(),
                    student.getLastName(),
                    student.getCreatedAt(),
                    student.getLastUpdatedAt(),
                    student.getBirthYear(),
                    student.getPhoneNumber(),
                    student.getEmail(),
                    student.getDescription(),
                    new SocialDto(
                            student.getSocials().getGithub(),
                            student.getSocials().getLinkedin(),
                            student.getSocials().getFacebook(),
                            student.getSocials().getInstagram()
                    ),
                    student.getPathToAvatar(),
                    student.getIndexNumber(),
                    student.getFaculty()
            );
        } else if (user instanceof Company company) {
            return new CompanyDto(
                    company.getClass().getSimpleName().toLowerCase(),
                    company.getUserId(),
                    company.getName(),
                    company.getCreatedAt(),
                    company.getLastUpdatedAt(),
                    company.getPhoneNumber(),
                    company.getEmail(),
                    company.getDescription(),
                    new SocialDto(
                            company.getSocials().getGithub(),
                            company.getSocials().getLinkedin(),
                            company.getSocials().getFacebook(),
                            company.getSocials().getInstagram()
                    ),
                    company.getPathToAvatar(),
                    company.getNip()
            );
        } else {
            College college = (College) user;
            return new CollegeDto(
                    college.getClass().getSimpleName().toLowerCase(),
                    college.getUserId(),
                    college.getName(),
                    college.getCreatedAt(),
                    college.getLastUpdatedAt(),
                    college.getEmail(),
                    college.getDescription(),
                    college.getPathToAvatar()
            );
        }
    }

    // TODO: findUsersPage
//    public Page<User> findUsersPage(Pageable pageable) {
//        return userRepository.findAll(pageable);
//    }

    // TODO: activate
    public void activate(String stringUUID) throws UUIDConversionException, ResourceNotFoundException {
        User user = findUserFromDBById(stringUUID);
        user.setActive(true);
        userRepository.save(user);
    }

    public void updateUser() {
        // TODO: update
    }

    public void changePassword(String stringUUID, PasswordChangeDto passwordChangeDto, String userEmail) throws ResourceNotFoundException, UUIDConversionException {
        User user = findUserFromDBById(stringUUID);
        if(user.getEmail().equals(userEmail)){
            throw new RuntimeException("User can only change own, profile.");
        }
        user.setPassword(passwordEncoder.encode(passwordChangeDto.password()));
        userRepository.save(user);
    }

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
}
