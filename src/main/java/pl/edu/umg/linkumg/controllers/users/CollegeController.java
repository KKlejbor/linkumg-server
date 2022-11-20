package pl.edu.umg.linkumg.controllers.users;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.edu.umg.linkumg.dtos.users.CollegeDto;
import pl.edu.umg.linkumg.dtos.users.register.CollegeRegisterDto;
import pl.edu.umg.linkumg.dtos.users.update.CollegeUpdateDto;
import pl.edu.umg.linkumg.exceptions.ResourceNotFoundException;
import pl.edu.umg.linkumg.exceptions.ResourceViolationException;
import pl.edu.umg.linkumg.service.UserService;

@RestController
@RequestMapping("/colleges")
@AllArgsConstructor
public class CollegeController {

    private UserService userService;

    @PostMapping
    public ResponseEntity<?> registerCollege(@RequestBody CollegeRegisterDto collegeRegisterDto) throws ResourceViolationException {
        userService.registerCollege(collegeRegisterDto);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PatchMapping
    public ResponseEntity<CollegeDto> updateCollege(@RequestBody CollegeUpdateDto collegeUpdateDto, Authentication authentication) throws ResourceNotFoundException {
        return ResponseEntity
                .ok()
                .body(userService.updateCollege(collegeUpdateDto, authentication.getName()));
    }
}
