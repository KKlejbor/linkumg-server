package pl.edu.umg.linkumg.validators.users;

import org.springframework.stereotype.Component;
import pl.edu.umg.linkumg.dtos.users.register.CollegeRegisterDto;
import pl.edu.umg.linkumg.exceptions.ValidationException;
import pl.edu.umg.linkumg.validators.DescriptionValidator;

@Component
public class CollegeValidator implements UserValidator, FacultyValidator, DescriptionValidator {

    @Override
    public void validateName(String name) throws ValidationException {
        boolean check = name.matches("[a-zA-Z.,ĄĆĘŁŃÓŚŹŻżąćęłńóśź ]{2,50}");
        if (!check) throw new ValidationException("Name is invalid.");
    }

    public void validateCollegeCreate(CollegeRegisterDto collegeRegisterDto) throws ValidationException {
        validateUserCreate(collegeRegisterDto.email(), collegeRegisterDto.password(), collegeRegisterDto.name());
        validateFaculty(collegeRegisterDto.faculty());
    }
}
