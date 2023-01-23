package pl.edu.umg.linkumg.validators.resources;

import org.springframework.stereotype.Component;
import pl.edu.umg.linkumg.dtos.resources.save.AnnouncementSaveDto;
import pl.edu.umg.linkumg.exceptions.ValidationException;

@Component
public class AnnouncementValidator implements ResourceValidator {
    public void validateAnnouncementCreate(AnnouncementSaveDto announcementSaveDto) throws ValidationException {
        validateTitle(announcementSaveDto.title());
        validateDescription(announcementSaveDto.description());
    }
}
