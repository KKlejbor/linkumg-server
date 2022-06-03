package pl.edu.umg.linkumg.controllers;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.edu.umg.linkumg.dto.AnnouncementDto;
import pl.edu.umg.linkumg.models.Announcement;
import pl.edu.umg.linkumg.service.impl.AnnouncementService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/")
    public List<AnnouncementDto> getAll()
    {
        return announcementService.findAll().stream().map(this::mapAnnouncementToDto).toList();
    }

    @GetMapping(value = "/{id}")
    public AnnouncementDto getAnnouncement(@PathVariable Integer id)
    {
        Optional<Announcement> announcement = announcementService.findById(id);

        if(announcement.isPresent())
            return mapAnnouncementToDto(announcement.get());

        return null;
    }

    @PostMapping(value = "/")
    public Announcement saveAnnouncement(@RequestBody AnnouncementDto announcementDto)
    {
        Announcement announcement = mapDtoToAnnouncement(announcementDto);

        announcement.setDate(new Date());


        return announcementService.saveOrUpdate(announcement);
    }

    @PutMapping(value = "/{id}")
    public Announcement updateAnnouncement(@PathVariable Integer id, @RequestBody AnnouncementDto announcementDto)
    {
        Optional<Announcement> announcement = announcementService.findById(id);

        if(announcement.isPresent())
        {
            Announcement getAnnouncement = announcement.get();
            getAnnouncement.setDate(new Date());
            getAnnouncement.setTitle(announcementDto.getTitle());
            getAnnouncement.setContent(announcementDto.getContent());
            getAnnouncement.setPathToAnnouncementImage(announcementDto.getPathToAnnouncementImage());
            return announcementService.saveOrUpdate(getAnnouncement);
        }

        return null;
    }

    @DeleteMapping(value = "/{id}")
    public void deleteAnnouncement(@PathVariable Integer id)
    {
        announcementService.deleteById(id);
    }

    private AnnouncementDto mapAnnouncementToDto (Announcement announcement)
    {
        return modelMapper.map(announcement, AnnouncementDto.class);
    }

    private Announcement mapDtoToAnnouncement(AnnouncementDto announcementDto)
    {
        return modelMapper.map(announcementDto, Announcement.class);
    }
}
