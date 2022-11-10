package pl.edu.umg.linkumg.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.edu.umg.linkumg.dto.JwtRequest;
import pl.edu.umg.linkumg.dto.PasswordChangeDto;
import pl.edu.umg.linkumg.dto.TokenDto;
import pl.edu.umg.linkumg.exception.*;
import pl.edu.umg.linkumg.models.User;
import pl.edu.umg.linkumg.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    /**
     * @param jwtRequest
     * @return {@link TokenDto}
     * @throws UserIsNotActivatedException
     * @throws UserIsBannedException
     * @throws UserBadCredentialsException
     */
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody JwtRequest jwtRequest) throws UserIsNotActivatedException, UserIsBannedException, UserBadCredentialsException {
        return ResponseEntity
                .accepted()
                .body(userService.generateToken(jwtRequest));
    }

//    @PostMapping("/refreshToken")
// public ResponseEntity<?> (){}

    public void logout(){

    }

    /**
     * @param stringUUID
     * @return
     * @throws ResourceNotFoundException
     * @throws UUIDConversionException
     */
    @GetMapping("/{stringUUID}")
    public ResponseEntity<?> getUser(@PathVariable String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        return ResponseEntity
                .ok()
                .body(userService.findUserById(stringUUID));
    }

    // TODO: 31.10.2022
    @GetMapping
    public ResponseEntity<List<?>> getUsers(@RequestParam(defaultValue = "1") int page) {
//        return userService.findUsersPage(page);
        return ResponseEntity
                .ok()
                .body(null);
    }

    @PostMapping("/remindPassword/{email}")
    public ResponseEntity.HeadersBuilder<?> remindPassword(@PathVariable String email) {
        userService.remaindPassword(email);
        return ResponseEntity.noContent();
    }

    @PatchMapping("/changePassword/{stringUUID}")
    public ResponseEntity.HeadersBuilder<?> changePassword(@PathVariable String stringUUID, @RequestBody PasswordChangeDto passwordChangeDto, Authentication authentication) throws ResourceNotFoundException, UUIDConversionException {
        userService.changePassword(stringUUID, passwordChangeDto, authentication.getName());
        return ResponseEntity.noContent();
    }

//    @PatchMapping
//    public User updateUser(@RequestBody , Authentication authentication) {
//        return ResponseEntity
//                .ok()
//                .body(userService.updateUser(authentication.getName()));
//    }

    @PatchMapping("/ban/{stringUUID}")
    public ResponseEntity.HeadersBuilder<?> banUser(@PathVariable String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        return ResponseEntity.noContent();
//                .body(userService.banUser(stringUUID));
    }
}
