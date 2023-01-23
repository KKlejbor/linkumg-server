package pl.edu.umg.linkumg.validators.resources;

import org.springframework.stereotype.Component;
import pl.edu.umg.linkumg.dtos.resources.save.PrivateLessonSaveDto;
import pl.edu.umg.linkumg.exceptions.ValidationException;

@Component
public class PrivateLessonValidator implements ResourceValidator {
    public void validatePrivateLessonCreate(PrivateLessonSaveDto privateLessonSaveDto) throws ValidationException {
        validateTitle(privateLessonSaveDto.title());
        validateDescription(privateLessonSaveDto.description());
        validateLocation(privateLessonSaveDto.location());
        validateCategory(privateLessonSaveDto.category());
        validatePricePerHour(privateLessonSaveDto.pricePerHour());
    }

    public void validateCategory(String category) throws ValidationException {
        boolean check = category.matches("[a-zA-ZĄĆĘŁŃÓŚŹŻżąćęłńóśź ]{2,20}");
        if (!check) throw new ValidationException("Category is invalid.");
    }

    public void validatePricePerHour(Integer pricePerHour) throws ValidationException {
        boolean check = pricePerHour.toString().matches("[0-9,. ]{1,8}") && pricePerHour >= 0;
        if (!check) throw new ValidationException("Price per hour is invalid.");
    }
}
