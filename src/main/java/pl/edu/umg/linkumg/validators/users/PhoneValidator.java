package pl.edu.umg.linkumg.validators.users;

import pl.edu.umg.linkumg.exceptions.ValidationException;

public interface PhoneValidator {
    default void validatePhone(String phone) throws ValidationException {
        boolean check = phone.matches("^[0-9]{9}$");
        if (!check) throw new ValidationException("Phone is invalid.");
    }
}
