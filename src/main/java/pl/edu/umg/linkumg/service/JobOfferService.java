package pl.edu.umg.linkumg.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.umg.linkumg.dto.JobOfferDto;
import pl.edu.umg.linkumg.exception.ResourceForbiddenException;
import pl.edu.umg.linkumg.exception.ResourceNotFoundException;
import pl.edu.umg.linkumg.exception.UUIDConversionException;
import pl.edu.umg.linkumg.models.Company;
import pl.edu.umg.linkumg.models.JobOffer;
import pl.edu.umg.linkumg.repositories.JobOfferRepository;
import pl.edu.umg.linkumg.repositories.UserRepository;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class JobOfferService {

    private JobOfferRepository jobOfferRepository;
    private UserRepository userRepository;
    private ObjectDtoConverter converter;

    public JobOfferDto createJobOffer(JobOfferDto jobOfferDto, String companyEmail) throws ResourceNotFoundException {
        Company company = checkCompanyExists(companyEmail);

        // TODO: validate

        JobOffer jobOffer = converter.jobOfferDtoToJobOfferObject(jobOfferDto);
        jobOffer.setCreatedAt(LocalDateTime.now());
        jobOffer.setLastUpdatedAt(LocalDateTime.now());
        jobOffer.setCompanyId(company.getUserId());
        jobOfferRepository.save(jobOffer);

        company.addJobOffer(jobOffer);
        company.setLastUpdatedAt(LocalDateTime.now());
        userRepository.save(company);

        return converter.jobOfferObjectToJobOfferDto(jobOffer);
    }

    public JobOfferDto findJobOfferById(String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        return converter.jobOfferObjectToJobOfferDto(
                checkJobOfferExists(stringUUID)
        );
    }

    // TODO: findPage
    public void findJobOfferPage(int page) {
    }

    public JobOfferDto updateJobOffer(String stringUUID, JobOfferDto jobOfferDto, String companyEmail) throws ResourceNotFoundException, UUIDConversionException, ResourceForbiddenException {
        JobOffer jobOffer = checkJobOfferExists(stringUUID);
        Company company = checkCompanyExists(companyEmail);
        checkCompanyHaveAccessToResource(company, jobOffer);

        // TODO: validation

        if (jobOfferDto.title() != null)
            jobOffer.setTitle(jobOfferDto.title());
        if (jobOfferDto.salaryDown() != null)
            jobOffer.setSalaryDown(jobOfferDto.salaryDown());
        if (jobOfferDto.salaryUp() != null)
            jobOffer.setSalaryUp(jobOfferDto.salaryUp());
        if (jobOfferDto.city() != null)
            jobOffer.setCity(jobOfferDto.city());
        if (jobOfferDto.pathToJobOfferImage() != null)
            jobOffer.setPathToJobOfferImage(jobOfferDto.pathToJobOfferImage());

        company.setLastUpdatedAt(LocalDateTime.now());

        userRepository.save(company);

        return converter.jobOfferObjectToJobOfferDto(jobOffer);
    }

    public void removeJobOffer(String stringUUID, String companyEmail) throws ResourceNotFoundException, UUIDConversionException, ResourceForbiddenException {
        JobOffer jobOffer = checkJobOfferExists(stringUUID);
        Company company = checkCompanyExists(companyEmail);
        checkCompanyHaveAccessToResource(company, jobOffer);

        jobOfferRepository.delete(jobOffer);
    }

    private JobOffer checkJobOfferExists(String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        return jobOfferRepository.findById(converter.stringUUIDToUUID(stringUUID))
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                String.format("Job offer with %s id not found.", stringUUID)
                        )
                );
    }

    private Company checkCompanyExists(String email) throws ResourceNotFoundException {
        return (Company) userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                String.format("User with email %s not found.", email)
                        )
                );
    }

    private void checkCompanyHaveAccessToResource(Company company, JobOffer jobOffer) throws ResourceForbiddenException {
        if (!jobOffer.getCompanyId().equals(company.getUserId()))
            throw new ResourceForbiddenException(String.format("User with email %s is forbidden to use this resource.", company.getEmail()));
    }
}
