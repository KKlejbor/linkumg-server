package pl.edu.umg.linkumg.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.umg.linkumg.dtos.resources.AnnouncementDto;
import pl.edu.umg.linkumg.dtos.resources.save.AnnouncementSaveDto;
import pl.edu.umg.linkumg.exceptions.ResourceForbiddenException;
import pl.edu.umg.linkumg.exceptions.ResourceNotFoundException;
import pl.edu.umg.linkumg.exceptions.UUIDConversionException;
import pl.edu.umg.linkumg.models.resources.Announcement;
import pl.edu.umg.linkumg.models.users.College;
import pl.edu.umg.linkumg.repositories.AnnouncementRepository;
import pl.edu.umg.linkumg.repositories.UserRepository;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AnnouncementService {

    private AnnouncementRepository announcementRepository;
    private UserRepository userRepository;

    public AnnouncementDto createAnnouncement(AnnouncementSaveDto announcementSaveDto, String collegeEmail) throws ResourceNotFoundException {
        College college = checkCollegeExists(collegeEmail);

        // TODO: validate announcementDto

        Announcement announcement = ObjectDtoConverter.announcementSaveDtoToAnnouncementObject(announcementSaveDto);
        announcement.setCreatedAt(LocalDateTime.now());
        announcement.setLastUpdatedAt(LocalDateTime.now());
        announcement.setCollege(college);

        announcementRepository.save(announcement);

        return ObjectDtoConverter.announcementObjectToAnnouncementDto(announcement);
    }

    public AnnouncementDto findAnnouncementById(String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        return ObjectDtoConverter.announcementObjectToAnnouncementDto(
                checkAnnouncementExists(stringUUID)
        );
    }

    public Page<AnnouncementDto> findAnnouncementsPage(Pageable pageable, String contain) {
        Page<Announcement> announcementsPage =
                announcementRepository.findAllByTitleContainingOrDescriptionContaining(pageable, contain, contain);

        return announcementsPage.map(ObjectDtoConverter::announcementObjectToAnnouncementDto);
    }

    @Transactional
    public AnnouncementDto updateAnnouncement(String stringUUID, AnnouncementSaveDto announcementSaveDto, String collegeEmail) throws ResourceNotFoundException, UUIDConversionException, ResourceForbiddenException {
        Announcement announcement = checkAnnouncementExists(stringUUID);
        College college = checkCollegeExists(collegeEmail);
        checkCollegeHaveAccessToResource(college, announcement);

        // TODO: validation

        if (announcementSaveDto.title() != null)
            announcement.setTitle(announcementSaveDto.title());
        if (announcementSaveDto.pathToAnnouncementImage() != null)
            announcement.setPathToAnnouncementImage(announcementSaveDto.pathToAnnouncementImage());
        if (announcementSaveDto.description() != null)
            announcement.setDescription(announcementSaveDto.description());

        announcement.setLastUpdatedAt(LocalDateTime.now());

        announcementRepository.save(announcement);

        return ObjectDtoConverter.announcementObjectToAnnouncementDto(announcement);
    }

    @Transactional
    public void removeAnnouncement(String stringUUID, String collegeEmail) throws ResourceNotFoundException, UUIDConversionException, ResourceForbiddenException {
        Announcement announcement = checkAnnouncementExists(stringUUID);
        College college = checkCollegeExists(collegeEmail);
        checkCollegeHaveAccessToResource(college, announcement);

        announcementRepository.delete(announcement);
    }

    private Announcement checkAnnouncementExists(String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        return announcementRepository.findById(ObjectDtoConverter.stringUUIDToUUID(stringUUID))
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                String.format("Announcement with %s id not found.", stringUUID)
                        )
                );
    }

    private College checkCollegeExists(String email) throws ResourceNotFoundException {
        return (College) userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                String.format("User with email %s not found.", email)
                        )
                );
    }

    private void checkCollegeHaveAccessToResource(College college, Announcement announcement) throws ResourceForbiddenException {
        if (!announcement.getCollege().getUserId().equals(college.getUserId()))
            throw new ResourceForbiddenException(String.format("User with email %s have forbidden access to this resource.", college.getEmail()));
    }
}
