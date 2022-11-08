package pl.edu.umg.linkumg.controllers;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.edu.umg.linkumg.dto.JwtRequest;
import pl.edu.umg.linkumg.exception.ResourceNotFoundException;
import pl.edu.umg.linkumg.exception.UUIDConversionException;
import pl.edu.umg.linkumg.exception.UserIsNotActivatedException;
import pl.edu.umg.linkumg.models.User;
import pl.edu.umg.linkumg.service.UserService;

import javax.annotation.security.PermitAll;
import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private static Logger LOGGER = LogManager.getLogger(UserController.class);

    private UserService userService;

    // TODO: refreshToken
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PermitAll
    public String login(@RequestBody JwtRequest jwtRequest) throws UserIsNotActivatedException {
        String token = userService.generateToken(jwtRequest);
        return token;
    }

    @GetMapping("/{stringUUID}")
    @ResponseStatus(HttpStatus.OK)
    public Object getUser(@PathVariable String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
            return userService.findUserById(stringUUID);
    }

    // TODO: 31.10.2022  
    @GetMapping
    public List<Object> getUsers(@RequestParam(defaultValue = "1") int page) {
//        return userService.findUsersPage(page);
        return null;
    }

    // TODO: 31.10.2022
    @PostMapping("/remindPassword/{email}")
    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    public void remindPassword(@PathVariable String email) {
    }

//    @PatchMapping("/changePassword")
//    @ResponseStatus(HttpStatus.OK)
//    @PermitAll
//    public void changePassword(@ResponseBody PasswordChangeDto passwordChangeDto, Authentication authentication) {
//        userService.changePassword(passwordChangeDto, authentication.getName());
//    }

    // TODO: 31.10.2022  
    @PatchMapping
    public User updateUser(Authentication authentication) {
        return null;
    }

    // TODO: ban user
    @PatchMapping("/{UUIDString}")
    public void banUser (@PathVariable String UUIDString) {

    }
}
