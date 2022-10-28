package pl.edu.umg.linkumg.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.umg.linkumg.dto.*;
import pl.edu.umg.linkumg.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private UserService userService;

    // TODO: 27.10.2022
    @PostMapping("/login")
    public ResponseEntity login(){
        return null;
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDto registerDto){
        System.out.println();
        return null;
    }

    @PostMapping("/remindPassword/{email}")
    public ResponseEntity remindPassword(@PathVariable String email){
        return null;
    }

    @PatchMapping("/changePassword/{UUIDString}")
    public void changePassword(@PathVariable String UUIDString){

    }

    @GetMapping("/{UUIDString}")
    public ResponseEntity getUser(@PathVariable String UUIDString) {
        return null;
    }

    @GetMapping
    public ResponseEntity getUsers(){
        return ResponseEntity.ok().body(new ArrayList());
    }

    @PatchMapping
    public ResponseEntity updateUser(){
        return null;
    }

    @DeleteMapping("/{UUIDString}")
    public void deleteUser(@PathVariable String UUIDString){

    }
}
