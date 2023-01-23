package pl.edu.umg.linkumg.validators.users;

import pl.edu.umg.linkumg.exceptions.ValidationException;

public interface FacultyValidator {
    default void validateFaculty(String faculty) throws ValidationException {
        boolean check = faculty.matches("[a-zA-ZĄĆĘŁŃÓŚŹŻżąćęłńóśź ]{2,40}");
        if (!check) throw new ValidationException("Faculty is invalid.");
    }
}
