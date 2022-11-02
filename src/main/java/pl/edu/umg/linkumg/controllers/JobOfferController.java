package pl.edu.umg.linkumg.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.edu.umg.linkumg.dto.JobOfferDto;
import pl.edu.umg.linkumg.exception.ResourceForbiddenException;
import pl.edu.umg.linkumg.exception.ResourceNotFoundException;
import pl.edu.umg.linkumg.exception.UUIDConversionException;
import pl.edu.umg.linkumg.service.JobOfferService;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/jobOffers")
@AllArgsConstructor
public class JobOfferController {

    private JobOfferService jobOfferService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RolesAllowed("ROLE_COMPANY")
    public JobOfferDto createJobOffer(@RequestBody JobOfferDto jobOfferDto, Authentication authentication) throws ResourceNotFoundException {
        return jobOfferService.createJobOffer(jobOfferDto, authentication.getName());
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
        jobOfferService.findJobOfferPage(page);
    }

    @PatchMapping("/{stringUUID}")
    @ResponseStatus(HttpStatus.OK)
    @RolesAllowed("ROLE_COMPANY")
    public JobOfferDto updateJobOffer(@PathVariable String stringUUID, @RequestBody JobOfferDto jobOfferDto, Authentication authentication) throws ResourceNotFoundException, UUIDConversionException, ResourceForbiddenException {
        return jobOfferService.updateJobOffer(stringUUID, jobOfferDto, authentication.getName());
    }

    @DeleteMapping("/{stringUUID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RolesAllowed("ROLE_COMPANY")
    public void removeJobOffer(@PathVariable String stringUUID, Authentication authentication) throws ResourceNotFoundException, UUIDConversionException, ResourceForbiddenException {
        jobOfferService.removeJobOffer(stringUUID, authentication.getName());
    }
}
