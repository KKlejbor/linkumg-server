package pl.edu.umg.linkumg;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.umg.linkumg.models.Announcement;
import pl.edu.umg.linkumg.models.College;
import pl.edu.umg.linkumg.repositories.AnnouncementRepository;
import pl.edu.umg.linkumg.repositories.PrivateLessonRepository;
import pl.edu.umg.linkumg.repositories.UserRepository;
import pl.edu.umg.linkumg.security.RsaKeyProperties;
import pl.edu.umg.linkumg.service.SendGridService;
import pl.edu.umg.linkumg.service.UserService;

import java.time.LocalDateTime;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class LinkumgApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinkumgApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(AnnouncementRepository announcementRepository, SendGridService service, UserService userService, UserRepository userRepository, PrivateLessonRepository privateLessonRepository, PasswordEncoder passwordEncoder) {
        return args -> {
//            Student student = userRepository.save(
//                    new Student(
//                            null,
//                            "student",
//                            passwordEncoder.encode("password"),
//                            true,
//                            false,
//                            "studentName",
//                            "description",
//                            "",
//                            LocalDateTime.now(),
//                            LocalDateTime.now(),
//                            "studentLastName",
//                            LocalDate.now(),
//                            "111 111 111",
//                            new Social(
//                                    null,
//                                    "github",
//                                    "linkedin",
//                                    "facebook",
//                                    null
//                            ),
//                            99999,
//                            "Inf"
//                    )
//            );
//            userRepository.save(student);
//            service.sendEmail();

            College college = userRepository.save(
                    new College(
                            null,
                            "college",
                            passwordEncoder.encode("password"),
                            true,
                            false,
                            "college",
                            "college",
                            "",
                            LocalDateTime.now(),
                            LocalDateTime.now(),
                            "Inf"
                    ));

            Announcement announcement =
                    new Announcement(
                            null,
                            "title",
                            "",
                            "content",
                            LocalDateTime.now(),
                            LocalDateTime.now(),
                            college.getUserId()
                    );

            announcementRepository.save(announcement);
            college.addAnnouncement(announcement);

            announcement.setTitle("content");
            announcement.setContent("title");
            announcement.setAnnouncementId(null);
            announcement.setCreatedAt(LocalDateTime.now());
            announcement.setLastUpdatedAt(LocalDateTime.now());
            announcementRepository.save(announcement);
            college.addAnnouncement(announcement);

            Announcement announcement3 =
                    new Announcement(
                            null,
                            "goblin",
                            "",
                            "pomidor",
                            LocalDateTime.now(),
                            LocalDateTime.now(),
                            college.getUserId()
                    );

            announcementRepository.save(announcement3);
            college.addAnnouncement(announcement3);

//            userRepository.save(college);
        };
    }
}
