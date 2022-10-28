package pl.edu.umg.linkumg.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.edu.umg.linkumg.dto.JobOfferDto;
import pl.edu.umg.linkumg.service.JobOfferService;

@RestController
@RequestMapping("/jobOffers")
@AllArgsConstructor
public class JobOfferController {

    private JobOfferService jobOfferService;

    // TODO: 28/10/2022
    @PostMapping
    public void makeJobOffer(@RequestBody JobOfferDto jobOfferDto){

    }

    @GetMapping("/{UUIDString}")
    public void getJobOfferById(@PathVariable String UUIDString){

    }

    @GetMapping
    public void getJobOfferPage(@RequestParam(defaultValue = "1") int page) {

    }

    @PatchMapping("/{UUIDString}")
    public void updateJobOffer(@PathVariable String UUIDString, @RequestBody JobOfferDto jobOfferDto){

    }

    @DeleteMapping("/{UUIDString}")
    public void removeJobOffer(@PathVariable String UUIDString){

    }
}
