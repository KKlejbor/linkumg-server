package pl.edu.umg.linkumg.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.edu.umg.linkumg.dto.JobOfferDto;
import pl.edu.umg.linkumg.exception.ResourceNotFoundException;
import pl.edu.umg.linkumg.exception.UUIDConversionException;
import pl.edu.umg.linkumg.service.JobOfferService;

@RestController
@RequestMapping("/jobOffers")
@AllArgsConstructor
public class JobOfferController {

    private JobOfferService jobOfferService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobOfferDto createJobOffer(@RequestBody JobOfferDto jobOfferDto) {
        return jobOfferService.createJobOffer(jobOfferDto);
    }

    @GetMapping("/{stringUUID}")
    @ResponseStatus(HttpStatus.OK)
    public JobOfferDto getJobOfferById(@PathVariable String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        return jobOfferService.findJobOfferById(stringUUID);
    }

    // TODO: findPage
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public void getJobOfferPage(@RequestParam(defaultValue = "1") int page) {

    }

    @PatchMapping("/{stringUUID}")
    @ResponseStatus(HttpStatus.OK)
    public JobOfferDto updateJobOffer(@PathVariable String stringUUID, @RequestBody JobOfferDto jobOfferDto) throws ResourceNotFoundException, UUIDConversionException {
        return jobOfferService.updateJobOffer(stringUUID, jobOfferDto);
    }

    @DeleteMapping("/{stringUUID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeJobOffer(@PathVariable String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        jobOfferService.removeJobOffer(stringUUID);
    }
}
