package pl.edu.umg.linkumg.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.edu.umg.linkumg.dtos.resources.JobOfferDto;
import pl.edu.umg.linkumg.dtos.resources.save.JobOfferSaveDto;
import pl.edu.umg.linkumg.exceptions.ResourceForbiddenException;
import pl.edu.umg.linkumg.exceptions.ResourceNotFoundException;
import pl.edu.umg.linkumg.exceptions.UUIDConversionException;
import pl.edu.umg.linkumg.models.resources.JobOffer;
import pl.edu.umg.linkumg.models.users.Company;
import pl.edu.umg.linkumg.repositories.JobOfferRepository;
import pl.edu.umg.linkumg.repositories.UserRepository;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class JobOfferService {

    private JobOfferRepository jobOfferRepository;
    private UserRepository userRepository;
    private ObjectDtoConverter converter;

    public JobOfferDto createJobOffer(JobOfferSaveDto jobOfferSaveDto, String companyEmail) throws ResourceNotFoundException {
        Company company = checkCompanyExists(companyEmail);

        // TODO: validate

        JobOffer jobOffer = converter.jobOfferSaveDtoToJobOfferObject(jobOfferSaveDto);
        jobOffer.setCreatedAt(LocalDateTime.now());
        jobOffer.setLastUpdatedAt(LocalDateTime.now());
        jobOffer.setCompany(company);

        jobOfferRepository.save(jobOffer);

        return converter.jobOfferObjectToJobOfferDto(jobOffer);
    }

    public JobOfferDto findJobOfferById(String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        return converter.jobOfferObjectToJobOfferDto(
                checkJobOfferExists(stringUUID)
        );
    }

    public Page<JobOfferDto> findJobOfferPage(Pageable pageable, String contain, String location, Integer salaryDown, Integer salaryUp) {
        Page<JobOffer> jobOffersPage;
        if (salaryDown != null || salaryUp != null) {
            if (salaryDown != null && salaryUp != null) {
                if (salaryDown > salaryUp) {
                    int store = salaryDown;
                    salaryDown = salaryUp;
                    salaryUp = store;
                }
                jobOffersPage = jobOfferRepository.findAllByTitleContainingOrDescriptionContainingOrLocationContainingAndSalaryDownIsGreaterThanEqualAndSalaryUpIsLessThanEqual(pageable, contain, contain, location, salaryDown, salaryUp);
            } else if (salaryDown != null) {
                jobOffersPage = jobOfferRepository.findAllByTitleContainingOrDescriptionContainingOrLocationContainingAndSalaryDownIsGreaterThanEqual(pageable, contain, contain, location, salaryDown);
            } else {
                jobOffersPage = jobOfferRepository.findAllByTitleContainingOrDescriptionContainingOrLocationContainingAndSalaryUpIsLessThanEqual(pageable, contain, contain, location, salaryUp);
            }
        } else {
            jobOffersPage = jobOfferRepository.findAllByTitleContainingOrDescriptionContainingOrLocationContaining(pageable, contain, contain, location);
        }
        return jobOffersPage.map(ObjectDtoConverter::jobOfferObjectToJobOfferDto);
    }

    public JobOfferDto updateJobOffer(String stringUUID, JobOfferSaveDto jobOfferSaveDto, String companyEmail) throws ResourceNotFoundException, UUIDConversionException, ResourceForbiddenException {
        JobOffer jobOffer = checkJobOfferExists(stringUUID);
        Company company = checkCompanyExists(companyEmail);
        checkCompanyHaveAccessToResource(company, jobOffer);

        // TODO: validation

        if (jobOfferSaveDto.title() != null)
            jobOffer.setTitle(jobOfferSaveDto.title());
        if (jobOfferSaveDto.salaryDown() != null)
            jobOffer.setSalaryDown(jobOfferSaveDto.salaryDown());
        if (jobOfferSaveDto.salaryUp() != null)
            jobOffer.setSalaryUp(jobOfferSaveDto.salaryUp());
        if (jobOfferSaveDto.location() != null)
            jobOffer.setLocation(jobOfferSaveDto.location());
        if (jobOfferSaveDto.description() != null)
            jobOffer.setDescription(jobOfferSaveDto.description());

        jobOfferRepository.save(jobOffer);

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
        if (!jobOffer.getCompany().getUserId().equals(company.getUserId()))
            throw new ResourceForbiddenException(String.format("User with email %s have forbidden access to this resource.", company.getEmail()));
    }
}
