package pl.edu.umg.linkumg.controllers.resources;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.edu.umg.linkumg.dtos.resources.AnnouncementDto;
import pl.edu.umg.linkumg.dtos.resources.save.AnnouncementSaveDto;
import pl.edu.umg.linkumg.exceptions.ResourceForbiddenException;
import pl.edu.umg.linkumg.exceptions.ResourceNotFoundException;
import pl.edu.umg.linkumg.exceptions.UUIDConversionException;
import pl.edu.umg.linkumg.service.AnnouncementService;

@RestController
@RequestMapping("/announcements")
@AllArgsConstructor
public class AnnouncementController {

    private AnnouncementService announcementService;

    @PostMapping
    public ResponseEntity<AnnouncementDto> createAnnouncement(@RequestBody AnnouncementSaveDto announcementDto, Authentication authentication) throws ResourceNotFoundException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(announcementService.createAnnouncement(announcementDto, authentication.getName()));
    }

    @GetMapping("/{stringUUID}")
    public ResponseEntity<AnnouncementDto> getAnnouncementById(@PathVariable String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        return ResponseEntity
                .ok()
                .body(announcementService.findAnnouncementById(stringUUID));
    }

    @GetMapping
    public ResponseEntity<Page<AnnouncementDto>> getAnnouncementPage(@RequestParam(defaultValue = "1") int page,
                                                                     @RequestParam(defaultValue = "10") int pageSize,
                                                                     @RequestParam(defaultValue = "asc") String direction,
                                                                     @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                     @RequestParam(defaultValue = "") String contain) {
        Direction directionObj = direction.equals("asc") ? Direction.ASC : Direction.DESC;
        String sortObj = sortBy.equals("lastUpdatedAt") ? "lastUpdatedAt" : "createdAt";
        Pageable pageable = PageRequest.of(page - 1, pageSize).withSort(directionObj, sortObj);
        return ResponseEntity
                .ok()
                .body(announcementService.findAnnouncementsPage(pageable, contain));
    }

    @PatchMapping("/{stringUUID}")
    public ResponseEntity<AnnouncementDto> updateAnnouncement(@PathVariable String stringUUID,
                                                              @RequestBody AnnouncementSaveDto announcementDto,
                                                              Authentication authentication) throws ResourceNotFoundException, UUIDConversionException, ResourceForbiddenException {
        return ResponseEntity
                .ok()
                .body(announcementService.updateAnnouncement(stringUUID, announcementDto, authentication.getName()));
    }

    @DeleteMapping("/{stringUUID}")
    public ResponseEntity<?> removeAnnouncement(@PathVariable String stringUUID, Authentication authentication) throws ResourceNotFoundException, UUIDConversionException, ResourceForbiddenException {
        announcementService.removeAnnouncement(stringUUID, authentication.getName());
        return ResponseEntity
                .noContent()
                .build();
    }
}
