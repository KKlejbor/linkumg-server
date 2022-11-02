package pl.edu.umg.linkumg.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.edu.umg.linkumg.dto.PrivateLessonDto;
import pl.edu.umg.linkumg.exception.ResourceForbiddenException;
import pl.edu.umg.linkumg.exception.ResourceNotFoundException;
import pl.edu.umg.linkumg.exception.UUIDConversionException;
import pl.edu.umg.linkumg.service.PrivateLessonService;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/privateLessons")
@AllArgsConstructor
public class PrivateLessonController {

    private PrivateLessonService privateLessonService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RolesAllowed(value = "ROLE_STUDENT")
    public PrivateLessonDto createPrivateLesson(@RequestBody PrivateLessonDto privateLessonDto, Authentication authentication) throws ResourceNotFoundException {
        return privateLessonService.createNewPrivateLesson(privateLessonDto, authentication.getName());
    }

    @GetMapping("/{stringUUID}")
    @ResponseStatus(HttpStatus.OK)
    public PrivateLessonDto getPrivateLessonById(@PathVariable String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        return privateLessonService.findPrivateLessonById(stringUUID);
    }

    // TODO: getPage
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public void getPrivateLessonPage(@RequestParam(defaultValue = "1") int page) {
        privateLessonService.findPrivateLessonsPage(page);
    }

    @PatchMapping("/{stringUUID}")
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed(value = "ROLE_STUDENT")
    public PrivateLessonDto updatePrivateLesson(@PathVariable String stringUUID, @RequestBody PrivateLessonDto privateLessonDto, Authentication authentication) throws ResourceNotFoundException, UUIDConversionException, ResourceForbiddenException {
        return privateLessonService.updatePrivateLesson(stringUUID, privateLessonDto, authentication.getName());
    }

    @DeleteMapping("/{stringUUID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RolesAllowed(value = "ROLE_STUDENT")
    public void removePrivateLesson(@PathVariable String stringUUID, Authentication authentication) throws ResourceNotFoundException, UUIDConversionException, ResourceForbiddenException {
        privateLessonService.removePrivateLesson(stringUUID, authentication.getName());
    }
}
