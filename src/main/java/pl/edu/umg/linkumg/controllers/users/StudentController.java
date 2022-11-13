package pl.edu.umg.linkumg.controllers.users;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.edu.umg.linkumg.dtos.register.StudentRegisterDto;
import pl.edu.umg.linkumg.exceptions.ResourceViolationException;
import pl.edu.umg.linkumg.service.UserService;

@RestController
@RequestMapping("/students")
@AllArgsConstructor
public class StudentController {

    private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerStudent(@RequestBody StudentRegisterDto studentRegisterDto) throws ResourceViolationException {
        userService.registerStudent(studentRegisterDto);
    }

    // TODO: updateStudent
    @PatchMapping
    public void updateStudent(){

    }
}
