package pl.edu.umg.linkumg.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.edu.umg.linkumg.dto.PrivateLessonDto;
import pl.edu.umg.linkumg.service.PrivateLessonService;

import java.util.UUID;

@RestController
@RequestMapping("/privateLessons")
@AllArgsConstructor
public class PrivateLessonController {

    private PrivateLessonService privateLessonService;

    // TODO: 28/10/2022
    @PostMapping
    public void makePrivateLesson(@RequestBody PrivateLessonDto privateLessonDto){

    }

    @GetMapping("/{UUIDString}")
    public void getPrivateLessonById(@PathVariable String UUIDString){

    }

    @GetMapping
    public void getPrivateLessonPage(@RequestParam(defaultValue = "1") int page) {

    }

    @PatchMapping("/{UUIDString}")
    public void updatePrivateLesson(@PathVariable String UUIDString, @RequestBody PrivateLessonDto privateLessonDto){

    }

    @DeleteMapping("/{UUIDString}")
    public void removePrivateLesson(@PathVariable String UUIDString){

    }
}
