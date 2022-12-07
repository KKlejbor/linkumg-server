package pl.edu.umg.linkumg.controllers.users;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.edu.umg.linkumg.dtos.users.CompanyDto;
import pl.edu.umg.linkumg.dtos.users.register.CompanyRegisterDto;
import pl.edu.umg.linkumg.dtos.users.update.CompanyUpdateDto;
import pl.edu.umg.linkumg.exceptions.ResourceNotFoundException;
import pl.edu.umg.linkumg.exceptions.ResourceViolationException;
import pl.edu.umg.linkumg.exceptions.ValidationException;
import pl.edu.umg.linkumg.service.UserService;

@RestController
@RequestMapping("/companies")
@AllArgsConstructor
public class CompanyController {

    private UserService userService;

    @PostMapping
    public ResponseEntity<?> registerCompany(@RequestBody CompanyRegisterDto companyRegisterDto) throws ResourceViolationException, ValidationException {
        userService.registerCompany(companyRegisterDto);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PatchMapping
    public ResponseEntity<CompanyDto> updateCompany(@RequestBody CompanyUpdateDto companyUpdateDto, Authentication authentication) throws ResourceNotFoundException, ValidationException {
        return ResponseEntity
                .ok()
                .body(userService.updateCompany(companyUpdateDto, authentication.getName()));
    }
}
