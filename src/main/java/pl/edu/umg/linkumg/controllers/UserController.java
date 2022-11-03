package pl.edu.umg.linkumg.controllers;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.umg.linkumg.dto.JwtRequest;
import pl.edu.umg.linkumg.dto.RegisterDto;
import pl.edu.umg.linkumg.exception.ResourceNotFoundException;
import pl.edu.umg.linkumg.exception.UUIDConversionException;
import pl.edu.umg.linkumg.service.UserService;

import javax.annotation.security.PermitAll;
import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private static Logger LOGGER = LogManager.getLogger(UserController.class);

    private UserService userService;

    // TODO: 31.10.2022
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PermitAll
    public String login(@RequestBody JwtRequest jwtRequest) {
        LOGGER.info("Trying to login with {}", jwtRequest);
        String token = userService.generateToken(jwtRequest);
        LOGGER.info("Generated token {}", token);
        return token;
    }

    // TODO: 31.10.2022  all fields
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @PermitAll
    public Object register(@RequestBody RegisterDto registerDto) {
        userService.register(registerDto);
        return registerDto;
    }

    // TODO: 31.10.2022  
    @PostMapping("/remindPassword/{email}")
    public ResponseEntity remindPassword(@PathVariable String email) {
        return null;
    }

    // TODO: 31.10.2022  
    @PatchMapping("/changePassword/{UUIDString}")
    public void changePassword(@PathVariable String UUIDString) {

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
    @PatchMapping
    public ResponseEntity updateUser() {
        return null;
    }

    // TODO: 31.10.2022  
    @DeleteMapping("/{UUIDString}")
    public void deleteUser(@PathVariable String UUIDString) {

    }
}
