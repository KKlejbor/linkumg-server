package pl.edu.umg.linkumg.controllers.resources;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.edu.umg.linkumg.dtos.resources.PrivateLessonDto;
import pl.edu.umg.linkumg.dtos.resources.save.PrivateLessonSaveDto;
import pl.edu.umg.linkumg.exceptions.ResourceForbiddenException;
import pl.edu.umg.linkumg.exceptions.ResourceNotFoundException;
import pl.edu.umg.linkumg.exceptions.UUIDConversionException;
import pl.edu.umg.linkumg.service.PrivateLessonService;

@RestController
@RequestMapping("/privateLessons")
@AllArgsConstructor
public class PrivateLessonController {

    private PrivateLessonService privateLessonService;

    @PostMapping
    public ResponseEntity<PrivateLessonDto> createPrivateLesson(@RequestBody PrivateLessonSaveDto privateLessonDto, Authentication authentication) throws ResourceNotFoundException {
        return ResponseEntity
                .ok()
                .body(privateLessonService.createNewPrivateLesson(privateLessonDto, authentication.getName()));
    }

    @GetMapping("/{stringUUID}")
    public ResponseEntity<PrivateLessonDto> getPrivateLessonById(@PathVariable String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        return ResponseEntity
                .ok()
                .body(privateLessonService.findPrivateLessonById(stringUUID));
    }

    @GetMapping
    public ResponseEntity<Page<PrivateLessonDto>> getPrivateLessonPage(@RequestParam(defaultValue = "1") int page,
                                                                       @RequestParam(defaultValue = "10") int pageSize,
                                                                       @RequestParam(defaultValue = "asc") String direction,
                                                                       @RequestParam(defaultValue = "createdAt") String sortBy,
                                                                       @RequestParam(defaultValue = "") String contain,
                                                                       @RequestParam(defaultValue = "") String category,
                                                                       @RequestParam(defaultValue = "") String location) {
        Sort.Direction directionObj = direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        String sortObj = switch (sortBy) {
            case "pricePerHour" -> "pricePerHour";
            case "lastUpdatedAt" -> "lastUpdatedAt";
            default -> "createdAt";
        };
        Pageable pageable = PageRequest.of(page - 1, pageSize).withSort(directionObj, sortObj);
        return ResponseEntity
                .ok()
                .body(privateLessonService.findPrivateLessonsPage(pageable, contain, category, location));
    }

    @PatchMapping("/{stringUUID}")
    public ResponseEntity<PrivateLessonDto> updatePrivateLesson(@PathVariable String stringUUID, @RequestBody PrivateLessonSaveDto privateLessonDto, Authentication authentication) throws ResourceNotFoundException, UUIDConversionException, ResourceForbiddenException {
        return ResponseEntity
                .ok()
                .body(privateLessonService.updatePrivateLesson(stringUUID, privateLessonDto, authentication.getName()));
    }

    @DeleteMapping("/{stringUUID}")
    public ResponseEntity<?> removePrivateLesson(@PathVariable String stringUUID, Authentication authentication) throws ResourceNotFoundException, UUIDConversionException, ResourceForbiddenException {
        privateLessonService.removePrivateLesson(stringUUID, authentication.getName());
        return ResponseEntity
                .noContent()
                .build();
    }
}
