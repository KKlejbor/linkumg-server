package pl.edu.umg.linkumg.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.edu.umg.linkumg.dto.PrivateLessonDto;
import pl.edu.umg.linkumg.models.PrivateLesson;
import pl.edu.umg.linkumg.service.impl.PrivateLessonService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/privatelessons")
public class PrivateLessonController {

    @Autowired
    private PrivateLessonService privateLessonService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/")
    public List<PrivateLessonDto> getAll()
    {
        return privateLessonService.findAll().stream().map(this::mapPrivateLessonToDto).toList();
    }

    @GetMapping(value = "/{id}")
    public PrivateLessonDto getPrivateLesson(@PathVariable Integer id)
    {
        Optional<PrivateLesson> privateLesson = privateLessonService.findById(id);

        if(privateLesson.isPresent())
            return mapPrivateLessonToDto(privateLesson.get());

        return null;
    }

    @PostMapping(value = "/")
    public PrivateLesson savePrivateLesson(@RequestBody PrivateLessonDto privateLessonDto)
    {
        PrivateLesson privateLesson = mapDtoToPrivateLesson(privateLessonDto);
        return privateLessonService.saveOrUpdate(privateLesson);
    }

    @PutMapping(value = "/{id}")
    public PrivateLesson updatePrivateLesson(@PathVariable Integer id, @RequestBody PrivateLessonDto privateLessonDto)
    {
        Optional<PrivateLesson> privateLesson = privateLessonService.findById(id);

        if(privateLesson.isPresent())
        {
            PrivateLesson getPrivateLesson = privateLesson.get();
            getPrivateLesson.setAddress(privateLessonDto.getAddress());
            getPrivateLesson.setCategory(privateLessonDto.getCategory());
            getPrivateLesson.setCity(privateLessonDto.getCity());
            getPrivateLesson.setPricePerHour(privateLessonDto.getPricePerHour());

            return privateLessonService.saveOrUpdate(getPrivateLesson);
        }
        return null;
    }

    @DeleteMapping(value = "/{id}")
    public void deletePrivateLesson(@PathVariable Integer id)
    {
        privateLessonService.deleteById(id);
    }

    private PrivateLessonDto mapPrivateLessonToDto (PrivateLesson privateLesson)
    {
        return modelMapper.map(privateLesson, PrivateLessonDto.class);
    }

    private PrivateLesson mapDtoToPrivateLesson(PrivateLessonDto privateLessonDto)
    {
        return modelMapper.map(privateLessonDto, PrivateLesson.class);
    }
}
