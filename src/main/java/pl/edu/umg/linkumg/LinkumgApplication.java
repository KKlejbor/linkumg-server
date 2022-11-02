package pl.edu.umg.linkumg;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.umg.linkumg.dto.RegisterDto;
import pl.edu.umg.linkumg.models.Social;
import pl.edu.umg.linkumg.models.Student;
import pl.edu.umg.linkumg.repositories.PrivateLessonRepository;
import pl.edu.umg.linkumg.repositories.UserRepository;
import pl.edu.umg.linkumg.security.RsaKeyProperties;
import pl.edu.umg.linkumg.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class LinkumgApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinkumgApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(UserService userService, UserRepository userRepository, PrivateLessonRepository privateLessonRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            userService.register(
                    new RegisterDto(
                            "student",
                            "studentemail",
                            passwordEncoder.encode("password"))
            );
            Student student = userRepository.save(
                    new Student(
                            null,
                            "student",
                            passwordEncoder.encode("password"),
                            true,
                            "studentName",
                            "description",
                            "",
                            LocalDateTime.now(),
                            LocalDateTime.now(),
                            "studentLastName",
                            LocalDate.now(),
                            "111 111 111",
                            new Social(
                                    null,
                                    "github",
                                    "linkedin",
                                    "facebook",
                                    null
                            ),
                            99999,
                            "Inf"
                    )
            );
            userRepository.save(student);
        };
    }
}
