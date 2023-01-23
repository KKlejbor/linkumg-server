package pl.edu.umg.linkumg.validators;

import pl.edu.umg.linkumg.exceptions.ValidationException;

public interface DescriptionValidator {
    default void validateDescription(String description) throws ValidationException {
        boolean check = description.matches("[a-zA-ZĄĆĘŁŃÓŚŹŻżąćęłńóśź0-9.,?!() ]{2,250}");
        if (!check)
            throw new ValidationException("Description is invalid.");
    }
}
