package pl.edu.umg.linkumg.service;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import pl.edu.umg.linkumg.dto.*;
import pl.edu.umg.linkumg.exception.ResourceNotFoundException;
import pl.edu.umg.linkumg.exception.UUIDConversionException;
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

    private static Logger LOGGER = LogManager.getLogger(UserService.class);

    private final JwtEncoder encoder;
    private final PasswordEncoder passwordEncoder;

    private UserRepository userRepository;
    private ObjectDtoConverter converter;

    // TODO: return UserDto
    public User register(RegisterDto registerDto) {
        if (registerDto.type().equals(Student.class.getSimpleName().toLowerCase())) {
            User student =
                    new Student(
                            registerDto.email(),
                            passwordEncoder.encode(registerDto.password())
                    );
            student.setCreatedAt(LocalDateTime.now());
            student.setLastUpdatedAt(LocalDateTime.now());
            return userRepository.save(student);
        } else if (registerDto.type().equals(Company.class.getSimpleName().toLowerCase())) {
            User employer =
                    new Company(
                            registerDto.email(),
                            passwordEncoder.encode(registerDto.password())
                    );
            return userRepository.save(employer);
        }
        // TODO: can we create college account?
        else if (registerDto.type().equals(College.class.getSimpleName().toLowerCase())) {
            User college =
                    new College(
                            registerDto.email(),
                            passwordEncoder.encode(registerDto.password())
                    );
            return userRepository.save(college);
        } else {
            // FIXME: create custom exception
            throw new RuntimeException("Wrong account type.");
        }
    }

    // TODO: activate
    public void activate() {
//        userRepository.findById(id)
//        userRepository.save()
    }

    // TODO: remaindPassword

    // TODO: returnTokenDto
    public String generateToken(JwtRequest jwtRequest) {
        User user =
                userRepository
                        .findByEmail(jwtRequest.email())
                        .orElseThrow(() -> new UsernameNotFoundException(String
                                .format("User with email %s not found.", jwtRequest.email())));
        LOGGER.info("User found in database {}", user);
        if (!passwordEncoder.matches(jwtRequest.password(), user.getPassword()))
            throw new BadCredentialsException("Bad credentials");
        if (!user.isActive()) {
            // FIXME: create custom exception
            throw new RuntimeException("User isn't activated.");
        }

        Instant now = Instant.now();
        String scope = user
                .getClass()
                .getSimpleName()
                .toUpperCase(Locale.ROOT);
        LOGGER.info("User roles is {}", scope);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.MINUTES))
                .subject(jwtRequest.email())
                .claim("scope", scope)
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    // TODO: update
    // TODO: changePassword
    // TODO: findUserById
    public Object findUserById(String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        User user = userRepository.findById(converter.stringUUIDToUUID(stringUUID))
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                String.format("User with id %s not found.", stringUUID)
                        )
                );

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
}
