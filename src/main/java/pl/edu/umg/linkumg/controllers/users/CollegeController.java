package pl.edu.umg.linkumg.controllers.users;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.umg.linkumg.dtos.register.CollegeRegisterDto;
import pl.edu.umg.linkumg.exceptions.ResourceViolationException;
import pl.edu.umg.linkumg.service.UserService;

@RestController
@RequestMapping("/colleges")
@AllArgsConstructor
public class CollegeController {

    private UserService userService;

    // TODO: registerCollege
    @PostMapping
    public ResponseEntity.BodyBuilder registerCollege(@RequestBody CollegeRegisterDto collegeRegisterDto) throws ResourceViolationException {
        userService.registerCollege(collegeRegisterDto);
        return ResponseEntity.ok();
    }

    // TODO: updateCollege
    @PatchMapping
    public void updateCollege(){

    }
}
