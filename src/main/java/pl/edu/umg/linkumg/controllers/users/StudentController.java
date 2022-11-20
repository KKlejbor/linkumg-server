package pl.edu.umg.linkumg.controllers.users;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.edu.umg.linkumg.dtos.users.StudentDto;
import pl.edu.umg.linkumg.dtos.users.register.StudentRegisterDto;
import pl.edu.umg.linkumg.dtos.users.update.StudentUpdateDto;
import pl.edu.umg.linkumg.exceptions.ResourceNotFoundException;
import pl.edu.umg.linkumg.exceptions.ResourceViolationException;
import pl.edu.umg.linkumg.service.UserService;

@RestController
@RequestMapping("/students")
@AllArgsConstructor
public class StudentController {

    private UserService userService;

    @PostMapping
    public ResponseEntity<?> registerStudent(@RequestBody StudentRegisterDto studentRegisterDto) throws ResourceViolationException {
        userService.registerStudent(studentRegisterDto);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PatchMapping
    public ResponseEntity<StudentDto> updateStudent(@RequestBody StudentUpdateDto studentUpdateDto, Authentication authentication) throws ResourceNotFoundException {
        return ResponseEntity
                .ok()
                .body(userService.updateStudent(studentUpdateDto, authentication.getName()));
    }
}
