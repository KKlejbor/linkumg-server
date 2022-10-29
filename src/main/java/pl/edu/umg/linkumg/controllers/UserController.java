package pl.edu.umg.linkumg.controllers;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import pl.edu.umg.linkumg.dto.JwtRequest;
import pl.edu.umg.linkumg.dto.RegisterDto;
import pl.edu.umg.linkumg.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
//@Log4j2
public class UserController {

    private static Logger LOGGER = LogManager.getLogger(UserController.class);

    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody JwtRequest jwtRequest) {
        LOGGER.info("Trying to login with {}", jwtRequest);
        String token = userService.generateToken(jwtRequest);
        LOGGER.info("Generated token {}", token);
        return ResponseEntity.ok().body(token);
    }

    @GetMapping("/student")
    public ResponseEntity student(Principal principal){
        return ResponseEntity.ok().body(String.format("Hello %s", principal.getName()));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDto registerDto) {
        return ResponseEntity.ok().body(registerDto);
    }

    @PostMapping("/remindPassword/{email}")
    public ResponseEntity remindPassword(@PathVariable String email) {
        return null;
    }

    @PatchMapping("/changePassword/{UUIDString}")
    public void changePassword(@PathVariable String UUIDString) {

    }

    @GetMapping("/{UUIDString}")
    public ResponseEntity getUser(@PathVariable String UUIDString) {
        return null;
    }

    @GetMapping
    public ResponseEntity getUsers(JwtAuthenticationToken principal) {
        LOGGER.info(principal);
        LOGGER.info(principal.getAuthorities());
        Collection<String> authorities = principal.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        LOGGER.info(authorities);
        return ResponseEntity.ok().body(new ArrayList());
    }

    @PatchMapping
    public ResponseEntity updateUser() {
        return null;
    }

    @DeleteMapping("/{UUIDString}")
    public void deleteUser(@PathVariable String UUIDString) {

    }
}
