package pl.edu.umg.linkumg.validators.users;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import pl.edu.umg.linkumg.dtos.users.register.StudentRegisterDto;
import pl.edu.umg.linkumg.exceptions.ValidationException;
import pl.edu.umg.linkumg.validators.DescriptionValidator;

@Component
// TODO: 06/12/2022  
@Primary
public class StudentValidator implements UserValidator, FacultyValidator, DescriptionValidator, PhoneValidator, SocialValidator {

    @Override
    public void validateName(String name) throws ValidationException {
        boolean check = name.matches("[A-ZĄĆĘŁŃÓŚŹŻ][a-zA-ZĄĆĘŁŃÓŚŹŻżąćęłńóśź]{1,19}");
        if (!check) throw new ValidationException("Name is invalid.");
    }

    public void validateStudentCreate(StudentRegisterDto studentRegisterDto) throws ValidationException {
        validateUserCreate(studentRegisterDto.email(), studentRegisterDto.password(), studentRegisterDto.name());
        validateFaculty(studentRegisterDto.faculty());
        validateIndexNumber(studentRegisterDto.indexNumber());
        validateName(studentRegisterDto.lastName());
    }

    private void validateIndexNumber(Integer indexNumber) throws ValidationException {
        boolean check = indexNumber.toString().matches("^[0-9]{5,10}$");
        if (!check)
            throw new ValidationException("Index number is invalid.");
    }
}
