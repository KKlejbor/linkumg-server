package pl.edu.umg.linkumg;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.umg.linkumg.dto.SocialDto;
import pl.edu.umg.linkumg.dto.StudentDto;
import pl.edu.umg.linkumg.models.Student;
import pl.edu.umg.linkumg.repositories.StudentRepository;
import pl.edu.umg.linkumg.repositories.UserRepository;
import pl.edu.umg.linkumg.security.RsaKeyProperties;
import pl.edu.umg.linkumg.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class LinkumgApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinkumgApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            studentRepository.save(
                    new Student(
                            UUID.randomUUID(),
                            "email@email.com",
                            passwordEncoder.encode("password"),
                            true,
                            "firstname",
                            "lastname",
                            "hello",
                            "role",
                            "",
                            99999,
                            "inf"
                    )
            );
        };
    }
}
