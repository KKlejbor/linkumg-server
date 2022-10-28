package pl.edu.umg.linkumg.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.edu.umg.linkumg.dto.AnnouncementDto;
import pl.edu.umg.linkumg.service.AnnouncementService;

import java.util.UUID;

@RestController
@RequestMapping("/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    private AnnouncementService announcementService;

    // TODO: 28/10/2022
    @PostMapping
    public void createAnnouncement(@RequestBody AnnouncementDto announcementDto) {

    }

    @GetMapping("/{stringUUID}")
    public void getAnnouncementById(@PathVariable String stringUUID) {
        UUID.fromString(stringUUID);
    }

    @GetMapping
    public void getAnnouncementPage(@RequestParam(defaultValue = "1") int page) {

    }

    @PatchMapping("/{stringUUID}")
    public void updateAnnouncement(@PathVariable String stringUUID, @RequestBody AnnouncementDto announcementDto) {

    }

    @DeleteMapping("/{stringUUID}")
    public void removeAnnouncement(@PathVariable String stringUUID) {

    }
}
