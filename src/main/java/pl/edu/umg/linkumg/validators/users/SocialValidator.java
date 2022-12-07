package pl.edu.umg.linkumg.validators.users;

import pl.edu.umg.linkumg.exceptions.ValidationException;

public interface SocialValidator {
    default void validateGithub(String github) throws ValidationException {
        boolean check = github.isBlank();
        if (!check) throw new ValidationException("Github is invalid.");
    }

    default void validateLinkedin(String linkedin) throws ValidationException {
        boolean check = linkedin.isBlank();
        if (!check) throw new ValidationException("Linkedin is invalid.");
    }

    default void validateFacebook(String facebook) throws ValidationException {
        boolean check = facebook.isBlank();
        if (!check) throw new ValidationException("Facebook is invalid.");
    }

    default void validateInstagram(String instagram) throws ValidationException {
        boolean check = instagram.isBlank();
        if (!check) throw new ValidationException("Instagram is invalid.");
    }

    default void validateOwnSite(String ownSite) throws ValidationException {
        boolean check = ownSite.isBlank();
        if (!check) throw new ValidationException("OwnSite is invalid.");
    }
}
