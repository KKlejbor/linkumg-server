package pl.edu.umg.linkumg.controllers.users;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.umg.linkumg.dtos.LoginDto;
import pl.edu.umg.linkumg.dtos.TokenDto;
import pl.edu.umg.linkumg.exceptions.*;
import pl.edu.umg.linkumg.service.UserService;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    /**
     * @param loginDto
     * @return {@link TokenDto}
     * @throws UserIsNotActivatedException
     * @throws UserIsBannedException
     * @throws UserBadCredentialsException
     */
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto) throws UserIsNotActivatedException, UserIsBannedException, UserBadCredentialsException {
        return ResponseEntity
                .accepted()
                .body(
                        userService.generateToken(loginDto)
                );
    }

    // TODO: renewAccessToken
    @PostMapping("/refreshToken")
    public ResponseEntity<?> renewAccessToken() {
        return null;
    }

    // TODO: logout
    public void logout() {

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

    @GetMapping("/activate")
    public ResponseEntity<?> activateAccount(@RequestParam String token) throws ResourceNotFoundException {
        userService.activate(token);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/remindPassword/{email}")
    public ResponseEntity<?> remindPassword(@PathVariable String email) {
        userService.remindPassword(email);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/ban/{stringUUID}")
    public ResponseEntity<?> banUser(@PathVariable String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        userService.banUser(stringUUID);
        return ResponseEntity.noContent().build();
    }
}
