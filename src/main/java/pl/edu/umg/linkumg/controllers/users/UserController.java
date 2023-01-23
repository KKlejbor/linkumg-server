package pl.edu.umg.linkumg.controllers.users;

import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.umg.linkumg.dtos.LoginDto;
import pl.edu.umg.linkumg.dtos.PasswordChangeDto;
import pl.edu.umg.linkumg.dtos.TokenDto;
import pl.edu.umg.linkumg.exceptions.*;
import pl.edu.umg.linkumg.service.ImageService;
import pl.edu.umg.linkumg.service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

@RestController
@RequestMapping("/users")
// TODO: deployement
//@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private ImageService imageService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto)
            throws UserIsNotActivatedException, UserIsBannedException, UserBadCredentialsException,
            ResourceNotFoundException {
        return ResponseEntity.accepted()
                             .body(userService.generateTokenByCredentials(loginDto));
    }

    @PostMapping("/renewToken")
    public ResponseEntity<TokenDto> renewAccessToken(String token)
            throws UserIsBannedException, UserIsNotActivatedException, ResourceNotFoundException,
            TokenExpiredException {
        return ResponseEntity.accepted()
                             .body(userService.renewToken(token));
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUser(Authentication authentication) throws ResourceNotFoundException {
        return ResponseEntity.ok()
                             .body(userService.findUserInfo(authentication.getName()));
    }

    @GetMapping("/{stringUUID}")
    public ResponseEntity<?> getUser(@PathVariable String stringUUID)
            throws ResourceNotFoundException, UUIDConversionException {
        return ResponseEntity.ok()
                             .body(userService.findUserById(stringUUID));
    }

    @GetMapping
    public ResponseEntity<Page<?>> getUsersPage(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "") String type, @RequestParam(defaultValue = "") String email) {
        Sort.Direction directionObj = direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        String sortObj = "email";
        Pageable pageable = PageRequest.of(page - 1, pageSize)
                                       .withSort(directionObj, sortObj);
        return ResponseEntity.ok()
                             .body(userService.findUsersPage(pageable, type, email));
    }

    @PostMapping("/remindPassword/{email}")
    public ResponseEntity<?> remindPassword(@PathVariable String email) throws ResourceNotFoundException {
        userService.remindPassword(email);
        return ResponseEntity.noContent()
                             .build();
    }

    @PatchMapping("/activate/{token}")
    public ResponseEntity<String> activateAccount(@PathVariable String token)
            throws ResourceNotFoundException, TokenExpiredException {
        userService.activate(token);
        return ResponseEntity.ok()
                             .body("Your account is activated.");
    }

    @PatchMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeDto passwordChangeDto,
            Authentication authentication) throws ResourceNotFoundException, ValidationException {
        userService.changePassword(passwordChangeDto, authentication.getName());
        return ResponseEntity.ok()
                             .body("Password changed.");
    }

    @PatchMapping("/changePassword/{token}")
    public ResponseEntity<String> changePasswordByRemindToken(@PathVariable String token,
            @RequestBody PasswordChangeDto passwordChangeDto)
            throws TokenExpiredException, ResourceNotFoundException, ValidationException {
        userService.changePasswordByRemindToken(passwordChangeDto, token);
        return ResponseEntity.ok()
                             .body("Password changed.");
    }

    @PatchMapping("/ban/{stringUUID}")
    public ResponseEntity<?> banUser(@PathVariable String stringUUID)
            throws ResourceNotFoundException, UUIDConversionException {
        userService.banUser(stringUUID);
        return ResponseEntity.noContent()
                             .build();
    }

    @PostMapping("/image/{stringUUID}")
    public ResponseEntity<String> saveUserImage(@PathVariable String stringUUID, @RequestParam MultipartFile image)
            throws IOException, UUIDConversionException {
        return ResponseEntity.ok()
                             .body(imageService.saveUserImage(stringUUID, image));
    }

    @GetMapping("/image/{fileName}")
    public ResponseEntity<InputStreamResource> getUserImage(@PathVariable String fileName) throws IOException {
        InputStream inputStream = imageService.getUserImage(fileName);
        return ResponseEntity.ok()
                             .contentType(MediaType.valueOf(URLConnection.guessContentTypeFromStream(inputStream)))
                             .body(new InputStreamResource(inputStream));
    }
}
