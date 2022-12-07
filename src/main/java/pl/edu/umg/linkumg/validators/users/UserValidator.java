package pl.edu.umg.linkumg.validators.users;

import org.springframework.stereotype.Component;
import pl.edu.umg.linkumg.dtos.PasswordChangeDto;
import pl.edu.umg.linkumg.exceptions.ValidationException;

@Component
public interface UserValidator {
    default void validateUserCreate(String email, String password, String name) throws ValidationException {
        validateEmail(email);
        validatePassword(password);
        validateName(name);
    }

    default void validateUserPasswordChange(PasswordChangeDto passwordChangeDto) throws ValidationException {
        validatePassword(passwordChangeDto.password());
    }

    default void validateEmail(String email) throws ValidationException {
        boolean check = email.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
        if (!check) throw new ValidationException("Email is invalid.");
    }

    default void validatePassword(String password) throws ValidationException {
        boolean check = password.matches(".{1,19}");
        if (!check) throw new ValidationException("Password is invalid.");
    }

    void validateName(String name) throws ValidationException;
}
