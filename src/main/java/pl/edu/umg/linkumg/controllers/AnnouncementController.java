package pl.edu.umg.linkumg.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.edu.umg.linkumg.dto.AnnouncementDto;
import pl.edu.umg.linkumg.exception.ResourceNotFoundException;
import pl.edu.umg.linkumg.exception.UUIDConversionException;
import pl.edu.umg.linkumg.service.AnnouncementService;

@RestController
@RequestMapping("/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    private AnnouncementService announcementService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnnouncementDto createAnnouncement(@RequestBody AnnouncementDto announcementDto) throws UUIDConversionException {
        return announcementService.createAnnouncement(announcementDto);
    }

    @GetMapping("/{stringUUID}")
    @ResponseStatus(HttpStatus.OK)
    public AnnouncementDto getAnnouncementById(@PathVariable String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        return announcementService.findAnnouncementById(stringUUID);
    }

    // TODO: findPage
    @GetMapping
    public void getAnnouncementPage(@RequestParam(defaultValue = "1") int page) {

    }

    @PatchMapping("/{stringUUID}")
    @ResponseStatus(HttpStatus.OK)
    public AnnouncementDto updateAnnouncement(@PathVariable String stringUUID, @RequestBody AnnouncementDto announcementDto) throws ResourceNotFoundException, UUIDConversionException {
        return announcementService.updateAnnouncement(stringUUID, announcementDto);
    }

    @DeleteMapping("/{stringUUID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAnnouncement(@PathVariable String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        announcementService.removeAnnouncement(stringUUID);
    }
}
