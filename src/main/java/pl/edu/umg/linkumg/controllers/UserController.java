package pl.edu.umg.linkumg.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.umg.linkumg.dto.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    StudentDto studentDto = new StudentDto(
            StudentDto.class.getSimpleName(),
            UUID.randomUUID(),
            "name",
            "lastname",
            LocalDateTime.now(),
            LocalDateTime.now(),
            LocalDate.now(),
            "560 656 654",
            "email@email.com",
            "hello",
            new SocialDto(null, null, "facebook.com", "instagram.com"),
            "",
            99999,
            "inf"
    );

    CompanyDto companyDto = new CompanyDto(
            CollegeDto.class.getSimpleName(),
            UUID.randomUUID(),
            "name",
            LocalDateTime.now(),
            LocalDateTime.now(),
            "560 656 654",
            "email@email.com",
            "hello",
            new SocialDto(null, null, "facebook.com", "instagram.com"),
            "",
            "nip"
    );

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

    // TODO: 27.10.2022
    @PostMapping("/remindPassword/{email}")
    public ResponseEntity remindPassword(@PathVariable String email){
        return null;
    }

    @GetMapping("/{uuidString}")
    public ResponseEntity getUser(@PathVariable String uuidString) {
        return null;
    }

    @GetMapping
    public ResponseEntity getUsers(){
        List<Object> objectList = Arrays.asList(studentDto, companyDto);
        return ResponseEntity.ok().body(objectList);
    }

    @PatchMapping
    public ResponseEntity updateUser(){
        return null;
    }
}
