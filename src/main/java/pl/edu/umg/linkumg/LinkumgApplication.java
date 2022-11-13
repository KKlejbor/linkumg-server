package pl.edu.umg.linkumg;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.umg.linkumg.models.users.College;
import pl.edu.umg.linkumg.models.users.Company;
import pl.edu.umg.linkumg.models.users.Student;
import pl.edu.umg.linkumg.repositories.UserRepository;
import pl.edu.umg.linkumg.security.RsaKeyProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class LinkumgApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinkumgApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Student student = new Student(
                    "student",
                    passwordEncoder.encode("password"),
                    "student",
                    "student",
                    99999,
                    "faculty"
            );

            student.setActive(true);

            userRepository.save(student);

            Company company = new Company(
                    "company",
                    passwordEncoder.encode("password"),
                    "company",
                    "nip"
            );

            company.setActive(true);

            userRepository.save(company);

            College college = new College(
                    "college",
                    passwordEncoder.encode("password"),
                    "college",
                    "faculty"
            );

            college.setActive(true);

            userRepository.save(college);
        };
    }
}
