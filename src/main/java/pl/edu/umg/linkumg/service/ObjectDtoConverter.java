package pl.edu.umg.linkumg.service;

import org.springframework.stereotype.Component;
import pl.edu.umg.linkumg.dto.AnnouncementDto;
import pl.edu.umg.linkumg.dto.JobOfferDto;
import pl.edu.umg.linkumg.dto.PrivateLessonDto;
import pl.edu.umg.linkumg.exception.UUIDConversionException;
import pl.edu.umg.linkumg.models.Announcement;
import pl.edu.umg.linkumg.models.JobOffer;
import pl.edu.umg.linkumg.models.PrivateLesson;

import java.util.UUID;

@Component
public class ObjectDtoConverter {

    public UUID stringUUIDToUUID(String stringUUID) throws UUIDConversionException {
        try {
            return UUID.fromString(stringUUID);
        } catch (IllegalArgumentException exception) {
            throw new UUIDConversionException(String.format("UUID %s isn't correct.", stringUUID));
        }
    }

    public Announcement announcementDtoToAnnouncementObject(AnnouncementDto announcementDto) throws UUIDConversionException {
        return new Announcement(
                stringUUIDToUUID(announcementDto.id()),
                announcementDto.title(),
                announcementDto.pathToAnnouncementImage(),
                announcementDto.content(),
                null,
                null,
                null
        );
    }

    public AnnouncementDto announcementObjectToAnnouncementDto(Announcement announcement) {
        return new AnnouncementDto(
                announcement.getAnnouncementId().toString(),
                announcement.getTitle(),
                announcement.getPathToAnnouncementImage(),
                announcement.getContent(),
                announcement.getCreatedAt().toString(),
                announcement.getLastUpdatedAt().toString(),
                announcement.getCollegeId().toString()
        );
    }

    public JobOffer jobOfferDtoToJobOfferObject(JobOfferDto jobOfferDto) {
        return null;
    }

    public JobOfferDto jobOfferObjectToJobOfferDto(JobOffer jobOffer) {
        return null;
    }

    public PrivateLesson privateLessonDtoToPrivateLessonObject(PrivateLessonDto privateLessonDto) {
        return new PrivateLesson(
                privateLessonDto.lessonId(),
                privateLessonDto.title(),
                privateLessonDto.category(),
                privateLessonDto.city(),
                privateLessonDto.pricePerHour(),
                privateLessonDto.description(),
                null,
                null,
                null
        );
    }

    public PrivateLessonDto privateLessonObjectToPrivateLessonDto(PrivateLesson privateLesson) {
        return new PrivateLessonDto(
                privateLesson.getLessonId(),
                privateLesson.getTitle(),
                privateLesson.getCategory(),
                privateLesson.getCity(),
                privateLesson.getPricePerHour(),
                privateLesson.getDescription(),
                privateLesson.getStudentId(),
                privateLesson.getCreatedAt(),
                privateLesson.getLastUpdatedAt()
        );
    }
}
