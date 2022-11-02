package pl.edu.umg.linkumg.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.umg.linkumg.dto.AnnouncementDto;
import pl.edu.umg.linkumg.exception.ResourceNotFoundException;
import pl.edu.umg.linkumg.exception.UUIDConversionException;
import pl.edu.umg.linkumg.repositories.AnnouncementRepository;

@Service
@AllArgsConstructor
public class AnnouncementService {

    private AnnouncementRepository announcementRepository;
    private ObjectDtoConverter converter;

    // TODO: create
    public AnnouncementDto createAnnouncement(AnnouncementDto announcementDto) throws UUIDConversionException {
        return converter.announcementObjectToAnnouncementDto(
                announcementRepository.save(
                        converter.announcementDtoToAnnouncementObject(announcementDto)
                )
        );
    }

    public AnnouncementDto findAnnouncementById(String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        return converter.announcementObjectToAnnouncementDto(
                announcementRepository.findById(converter.stringUUIDToUUID(stringUUID))
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        String.format("Announcement with %s id not found.", stringUUID)
                                )
                        )
        );
    }

    // TODO: findPage
    // TODO: update
    public AnnouncementDto updateAnnouncement(String stringUUID, AnnouncementDto announcementDto) throws ResourceNotFoundException {
        return null;
    }

    public void removeAnnouncement(String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        announcementRepository.delete(
                announcementRepository.findById(converter.stringUUIDToUUID(stringUUID))
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        String.format("Announcement with %s id not found.", stringUUID)
                                )
                        )
        );
    }
}
