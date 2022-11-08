package pl.edu.umg.linkumg.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.edu.umg.linkumg.dto.AnnouncementDto;
import pl.edu.umg.linkumg.exception.ResourceForbiddenException;
import pl.edu.umg.linkumg.exception.ResourceNotFoundException;
import pl.edu.umg.linkumg.exception.UUIDConversionException;
import pl.edu.umg.linkumg.models.Announcement;
import pl.edu.umg.linkumg.models.College;
import pl.edu.umg.linkumg.repositories.AnnouncementRepository;
import pl.edu.umg.linkumg.repositories.UserRepository;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AnnouncementService {

    private AnnouncementRepository announcementRepository;
    private UserRepository userRepository;
    private ObjectDtoConverter converter;

    public AnnouncementDto createAnnouncement(AnnouncementDto announcementDto, String collegeEmail) throws UUIDConversionException, ResourceNotFoundException {
        College college = checkCollegeExists(collegeEmail);

        // TODO: validate announcementDto

        Announcement announcement = converter.announcementDtoToAnnouncementObject(announcementDto);
        announcement.setCreatedAt(LocalDateTime.now());
        announcement.setLastUpdatedAt(LocalDateTime.now());
        announcement.setCollegeId(college.getUserId());
        announcementRepository.save(announcement);

        college.addAnnouncement(announcement);
        college.setLastUpdatedAt(LocalDateTime.now());
        userRepository.save(college);

        return converter.announcementObjectToAnnouncementDto(announcement);
    }

    public AnnouncementDto findAnnouncementById(String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        return converter.announcementObjectToAnnouncementDto(checkAnnouncementExists(stringUUID));
    }

    // TODO: findPage
    public Page<AnnouncementDto> findAnnouncementsPage(Pageable pageable, String search) {
        Page<Announcement> announcementsPage = null;

        if (search.isEmpty()) {
            announcementRepository.findAll(pageable);
        } else {
            announcementRepository.findAllByTitleOrContentContaining(pageable, search, search);
        }

        return announcementsPage.map(ObjectDtoConverter::announcementObjectToAnnouncementDto);
    }

    public AnnouncementDto updateAnnouncement(String stringUUID, AnnouncementDto announcementDto, String collegeEmail) throws ResourceNotFoundException, UUIDConversionException, ResourceForbiddenException {
        Announcement announcement = checkAnnouncementExists(stringUUID);
        College college = checkCollegeExists(collegeEmail);
        checkCollegeHaveAccessToResource(college, announcement);

        // TODO: validation

        if (announcementDto.title() != null)
            announcement.setTitle(announcementDto.title());
        if (announcementDto.pathToAnnouncementImage() != null)
            announcement.setPathToAnnouncementImage(announcementDto.pathToAnnouncementImage());
        if (announcementDto.content() != null)
            announcement.setContent(announcementDto.content());

        announcement.setLastUpdatedAt(LocalDateTime.now());

        announcementRepository.save(announcement);

        return converter.announcementObjectToAnnouncementDto(announcement);
    }

    public void removeAnnouncement(String stringUUID, String collegeEmail) throws ResourceNotFoundException, UUIDConversionException, ResourceForbiddenException {
        Announcement announcement = checkAnnouncementExists(stringUUID);
        College college = checkCollegeExists(collegeEmail);
        checkCollegeHaveAccessToResource(college, announcement);

        announcementRepository.delete(announcement);
    }

    private Announcement checkAnnouncementExists(String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        return announcementRepository.findById(converter.stringUUIDToUUID(stringUUID))
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
        if (!announcement.getCollegeId().equals(college.getUserId()))
            throw new ResourceForbiddenException(String.format("User with email %s is forbidden to use this resource.", college.getEmail()));
    }
}
