package pl.edu.umg.linkumg.controllers.users;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.edu.umg.linkumg.dtos.register.CompanyRegisterDto;
import pl.edu.umg.linkumg.exceptions.ResourceViolationException;
import pl.edu.umg.linkumg.service.UserService;

@RestController
@RequestMapping("/companies")
@AllArgsConstructor
public class CompanyController {

    private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerCompany(@RequestBody CompanyRegisterDto companyRegisterDto) throws ResourceViolationException {
        userService.registerCompany(companyRegisterDto);
    }

    // TODO: updateCompany
    @PatchMapping
    public void updateCompany(){

    }
}
