package pl.edu.umg.linkumg.controllers.users;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.edu.umg.linkumg.dtos.LoginDto;
import pl.edu.umg.linkumg.dtos.PasswordChangeDto;
import pl.edu.umg.linkumg.dtos.TokenDto;
import pl.edu.umg.linkumg.exceptions.*;
import pl.edu.umg.linkumg.service.UserService;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto) throws UserIsNotActivatedException, UserIsBannedException, UserBadCredentialsException, ResourceNotFoundException {
        return ResponseEntity
                .accepted()
                .body(userService.generateTokenByCredentials(loginDto));
    }

    @PostMapping("/renewToken")
    public ResponseEntity<TokenDto> renewAccessToken(String token) throws UserIsBannedException, UserIsNotActivatedException, ResourceNotFoundException, TokenExpiredException {
        return ResponseEntity
                .accepted()
                .body(userService.renewToken(token));
    }

    // TODO: logout
    public ResponseEntity<?> logout() {
        userService.logout();
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/{stringUUID}")
    public ResponseEntity<?> getUser(@PathVariable String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        return ResponseEntity
                .ok()
                .body(userService.findUserById(stringUUID));
    }

    // TODO: getUsersPage
    @GetMapping
    public ResponseEntity<Page<?>> getUsersPage(@RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "10") int pageSize,
                                                @RequestParam(defaultValue = "asc") String direction,
                                                @RequestParam(defaultValue = "") String search) {
        return ResponseEntity
                .ok()
                .body(userService.findUsersPage(
                        PageRequest.of(page, pageSize)
                ));
    }

    @PostMapping("/remindPassword/{email}")
    public ResponseEntity<?> remindPassword(@PathVariable String email) throws ResourceNotFoundException {
        userService.remindPassword(email);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PatchMapping("/activate/{token}")
    public ResponseEntity<String> activateAccount(@PathVariable String token) throws ResourceNotFoundException, TokenExpiredException {
        userService.activate(token);
        return ResponseEntity
                .ok()
                .body("Your account is activated.");
    }

    @PatchMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeDto passwordChangeDto, Authentication authentication) throws ResourceNotFoundException {
        userService.changePassword(passwordChangeDto, authentication.getName());
        return ResponseEntity
                .ok()
                .body("Password changed.");
    }

    @PatchMapping("/changePassword/{token}")
    public ResponseEntity<String> changePasswordByRemindToken(@PathVariable String token, @RequestBody PasswordChangeDto passwordChangeDto) throws TokenExpiredException, ResourceNotFoundException {
        userService.changePasswordByRemindToken(passwordChangeDto, token);
        return ResponseEntity
                .ok()
                .body("Password changed.");
    }

    @PatchMapping("/ban/{stringUUID}")
    public ResponseEntity<?> banUser(@PathVariable String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        userService.banUser(stringUUID);
        return ResponseEntity
                .noContent()
                .build();
    }
}
