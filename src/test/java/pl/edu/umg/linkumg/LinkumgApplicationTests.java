package pl.edu.umg.linkumg;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.edu.umg.linkumg.models.Student;
import pl.edu.umg.linkumg.models.User;
import pl.edu.umg.linkumg.service.StudentService;
import pl.edu.umg.linkumg.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class LinkumgApplicationTests {

    @Autowired
    private StudentService studentService;
    @Autowired
    private UserService userService;

    private Student student = new Student(
            null,
            "email",
            "password",
            true,
            "John",
            "Smith",
            "description",
            "student",
            "",
            99999,
            "inf"
    );

    @Test
    void createStudentObjectInDatabase() {
        //given
//        Student student = new Student(
//                null,
//                "email",
//                "password",
//                true,
//                "John",
//                "Smith",
//                "description",
//                "student",
//                "",
//                99999,
//                "inf"
//        );

        //when
        studentService.save(student);

        //then
        assertNotNull(student.getUserId());
    }

    @Test
    void findUserPage() {
        //given
        Pageable pageable = PageRequest.of(0, 8);
        student.setUserId(null);
        student.setEmail(student.getEmail() + 1);
        studentService.save(student);
        student.setUserId(null);
        student.setEmail(student.getEmail() + 2);
        studentService.save(student);
        student.setUserId(null);
        student.setEmail(student.getEmail() + 3);
        studentService.save(student);
        //when
        Page<User> userPage = userService.findUsersPage(pageable);
        //then
        assertEquals(3, userPage.getContent().size());
        assertEquals(3, userPage.getNumberOfElements());
    }

}
