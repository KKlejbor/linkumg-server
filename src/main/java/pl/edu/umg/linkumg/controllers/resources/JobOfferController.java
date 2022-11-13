package pl.edu.umg.linkumg.controllers.resources;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.edu.umg.linkumg.dtos.resources.JobOfferDto;
import pl.edu.umg.linkumg.dtos.resources.save.JobOfferSaveDto;
import pl.edu.umg.linkumg.exceptions.ResourceForbiddenException;
import pl.edu.umg.linkumg.exceptions.ResourceNotFoundException;
import pl.edu.umg.linkumg.exceptions.UUIDConversionException;
import pl.edu.umg.linkumg.service.JobOfferService;

@RestController
@RequestMapping("/jobOffers")
@AllArgsConstructor
public class JobOfferController {

    private JobOfferService jobOfferService;

    @PostMapping
    public ResponseEntity<JobOfferDto> createJobOffer(@RequestBody JobOfferSaveDto jobOfferDto, Authentication authentication) throws ResourceNotFoundException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        jobOfferService.createJobOffer(jobOfferDto, authentication.getName())
                );
    }

    @GetMapping("/{stringUUID}")
    public ResponseEntity<JobOfferDto> getJobOfferById(@PathVariable String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        return ResponseEntity
                .ok()
                .body(
                        jobOfferService.findJobOfferById(stringUUID)
                );
    }

    @GetMapping
    public ResponseEntity<Page<JobOfferDto>> getJobOfferPage(@RequestParam(defaultValue = "1") int page,
                                                             @RequestParam(defaultValue = "10") int pageSize,
                                                             @RequestParam(defaultValue = "asc") String direction,
                                                             @RequestParam(defaultValue = "createdAt") String sortBy,
                                                             @RequestParam(defaultValue = "") String contain,
                                                             @RequestParam(defaultValue = "") String location,
                                                             @RequestParam(defaultValue = "") Integer salaryDown,
                                                             @RequestParam(defaultValue = "") Integer salaryUp) {
        Sort.Direction directionObj = direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        String sortObj = sortBy.equals("lastUpdatedAt") ? "lastUpdatedAt" : "createdAt";
        Pageable pageable = PageRequest.of(page - 1, pageSize).withSort(directionObj, sortObj);
        return ResponseEntity
                .ok()
                .body(
                        jobOfferService.findJobOfferPage(pageable, contain, location, salaryDown, salaryUp)
                );

    }

    @PatchMapping("/{stringUUID}")
    public ResponseEntity<JobOfferDto> updateJobOffer(@PathVariable String stringUUID, @RequestBody JobOfferSaveDto jobOfferDto, Authentication authentication) throws ResourceNotFoundException, UUIDConversionException, ResourceForbiddenException {
        return ResponseEntity
                .ok()
                .body(
                        jobOfferService.updateJobOffer(stringUUID, jobOfferDto, authentication.getName())
                );
    }

    @DeleteMapping("/{stringUUID}")
    public ResponseEntity<?> removeJobOffer(@PathVariable String stringUUID, Authentication authentication) throws ResourceNotFoundException, UUIDConversionException, ResourceForbiddenException {
        jobOfferService.removeJobOffer(stringUUID, authentication.getName());
        return ResponseEntity
                .noContent()
                .build();
    }
}
