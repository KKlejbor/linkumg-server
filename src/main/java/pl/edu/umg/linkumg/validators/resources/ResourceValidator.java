package pl.edu.umg.linkumg.validators.resources;

import pl.edu.umg.linkumg.exceptions.ValidationException;
import pl.edu.umg.linkumg.validators.DescriptionValidator;

public interface ResourceValidator extends DescriptionValidator {
    default void validateTitle(String title) throws ValidationException {
        boolean check = title.matches("[a-zA-ZĄĆĘŁŃÓŚŹŻżąćęłńóśź ]{2,50}");
        if (!check) throw new ValidationException("Title is invalid.");
    }

    default void validateLocation(String location) throws ValidationException {
        boolean check = location.matches("[A-ZĄĆĘŁŃÓŚŹŻ][a-zżąćęłńóśź ]{2,19}");
        if (!check) throw new ValidationException("Location is invalid.");
    }
}
