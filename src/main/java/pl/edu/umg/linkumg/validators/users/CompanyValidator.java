package pl.edu.umg.linkumg.validators.users;

import org.springframework.stereotype.Component;
import pl.edu.umg.linkumg.dtos.users.register.CompanyRegisterDto;
import pl.edu.umg.linkumg.exceptions.ValidationException;
import pl.edu.umg.linkumg.validators.DescriptionValidator;

@Component
public class CompanyValidator implements UserValidator, DescriptionValidator, PhoneValidator, SocialValidator {

    @Override
    public void validateName(String name) throws ValidationException {
        boolean check = name.matches("^[a-z]|\\d?[a-zA-Z0-9]?[a-zA-Z0-9\\s&@.]+$");
        if (!check) throw new ValidationException("Name is invalid.");
    }

    public void validateCompanyCreate(CompanyRegisterDto companyRegisterDto) throws ValidationException {
        validateUserCreate(companyRegisterDto.email(), companyRegisterDto.password(), companyRegisterDto.name());
        validateNip(companyRegisterDto.nip());
    }

    public void validateNip(String nip) throws ValidationException {
        boolean check = nip.matches("^[0-9]{10}$");
        if (!check) throw new ValidationException("NIP is invalid.");
    }
}
