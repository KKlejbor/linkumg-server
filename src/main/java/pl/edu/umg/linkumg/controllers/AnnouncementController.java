package pl.edu.umg.linkumg.controllers;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.edu.umg.linkumg.dto.AnnouncementDto;
import pl.edu.umg.linkumg.exception.ResourceForbiddenException;
import pl.edu.umg.linkumg.exception.ResourceNotFoundException;
import pl.edu.umg.linkumg.exception.UUIDConversionException;
import pl.edu.umg.linkumg.service.AnnouncementService;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/announcements")
@AllArgsConstructor
public class AnnouncementController {

    private AnnouncementService announcementService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RolesAllowed("ROLE_COLLEGE")
    public AnnouncementDto createAnnouncement(@RequestBody AnnouncementDto announcementDto, Authentication authentication) throws UUIDConversionException, ResourceNotFoundException {
        return announcementService.createAnnouncement(announcementDto, authentication.getName());
    }

    @GetMapping("/{stringUUID}")
    @ResponseStatus(HttpStatus.OK)
    public AnnouncementDto getAnnouncementById(@PathVariable String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        return announcementService.findAnnouncementById(stringUUID);
    }

    @GetMapping
    public List<AnnouncementDto> getAnnouncementPage(@RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "10") int pageSize,
                                                     @RequestParam(defaultValue = "asc") String direction,
                                                     @RequestParam(defaultValue = "") String search) {
        Direction directionObj = direction.equals("asc") ? Direction.ASC : Direction.DESC;
        Pageable pageable = PageRequest.of(page - 1, pageSize).withSort(directionObj, "createdAt");
        return announcementService.findAnnouncementsPage(pageable, search).getContent();
    }

    @PatchMapping("/{stringUUID}")
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed("ROLE_COLLEGE")
    public AnnouncementDto updateAnnouncement(@PathVariable String stringUUID, @RequestBody AnnouncementDto announcementDto, Authentication authentication) throws ResourceNotFoundException, UUIDConversionException, ResourceForbiddenException {
        return announcementService.updateAnnouncement(stringUUID, announcementDto, authentication.getName());
    }

    @DeleteMapping("/{stringUUID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RolesAllowed("ROLE_COLLEGE")
    public void removeAnnouncement(@PathVariable String stringUUID, Authentication authentication) throws ResourceNotFoundException, UUIDConversionException, ResourceForbiddenException {
        announcementService.removeAnnouncement(stringUUID, authentication.getName());
    }
}
