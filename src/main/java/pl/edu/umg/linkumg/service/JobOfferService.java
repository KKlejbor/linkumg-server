package pl.edu.umg.linkumg.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.umg.linkumg.dtos.resources.JobOfferDto;
import pl.edu.umg.linkumg.dtos.resources.save.JobOfferSaveDto;
import pl.edu.umg.linkumg.exceptions.ResourceForbiddenException;
import pl.edu.umg.linkumg.exceptions.ResourceNotFoundException;
import pl.edu.umg.linkumg.exceptions.UUIDConversionException;
import pl.edu.umg.linkumg.exceptions.ValidationException;
import pl.edu.umg.linkumg.models.resources.JobOffer;
import pl.edu.umg.linkumg.models.users.Company;
import pl.edu.umg.linkumg.repositories.JobOfferRepository;
import pl.edu.umg.linkumg.repositories.UserRepository;
import pl.edu.umg.linkumg.validators.resources.JobOfferValidator;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class JobOfferService {

    private JobOfferRepository jobOfferRepository;
    private UserRepository userRepository;
    private JobOfferValidator jobOfferValidator;

    public JobOfferDto createJobOffer(JobOfferSaveDto jobOfferSaveDto, String companyEmail) throws ResourceNotFoundException, ValidationException {
        Company company = checkCompanyExists(companyEmail);

        jobOfferValidator.validateJobOfferCreate(jobOfferSaveDto);

        JobOffer jobOffer = ObjectDtoConverter.jobOfferSaveDtoToJobOfferObject(jobOfferSaveDto);
        jobOffer.setCreatedAt(LocalDateTime.now());
        jobOffer.setLastUpdatedAt(LocalDateTime.now());
        jobOffer.setCompany(company);

        jobOfferRepository.save(jobOffer);

        return ObjectDtoConverter.jobOfferObjectToJobOfferDto(jobOffer);
    }

    public JobOfferDto findJobOfferById(String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        return ObjectDtoConverter.jobOfferObjectToJobOfferDto(
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

    @Transactional
    public JobOfferDto updateJobOffer(String stringUUID, JobOfferSaveDto jobOfferSaveDto, String companyEmail) throws ResourceNotFoundException, UUIDConversionException, ResourceForbiddenException, ValidationException {
        JobOffer jobOffer = checkJobOfferExists(stringUUID);
        Company company = checkCompanyExists(companyEmail);
        checkCompanyHaveAccessToResource(company, jobOffer);

        int changes = 0;

        if (jobOfferSaveDto.title() != null) {
            jobOfferValidator.validateTitle(jobOfferSaveDto.title());
            jobOffer.setTitle(jobOfferSaveDto.title());
            changes++;
        }
        if (jobOfferSaveDto.salaryDown() != null) {
            jobOfferValidator.validateSalaryDown(jobOfferSaveDto.salaryDown(), jobOffer.getSalaryUp());
            jobOffer.setSalaryDown(jobOfferSaveDto.salaryDown());
            changes++;
        }
        if (jobOfferSaveDto.salaryUp() != null) {
            jobOfferValidator.validateSalaryUp(jobOfferSaveDto.salaryUp(), jobOffer.getSalaryDown());
            jobOffer.setSalaryUp(jobOfferSaveDto.salaryUp());
            changes++;
        }
        if (jobOfferSaveDto.location() != null) {
            jobOfferValidator.validateLocation(jobOfferSaveDto.location());
            jobOffer.setLocation(jobOfferSaveDto.location());
            changes++;
        }
        if (jobOfferSaveDto.description() != null) {
            jobOfferValidator.validateDescription(jobOfferSaveDto.description());
            jobOffer.setDescription(jobOfferSaveDto.description());
            changes++;
        }

        if(changes>0) {
            jobOffer.setLastUpdatedAt(LocalDateTime.now());
            jobOfferRepository.save(jobOffer);
        }

        return ObjectDtoConverter.jobOfferObjectToJobOfferDto(jobOffer);
    }

    @Transactional
    public void removeJobOffer(String stringUUID, String companyEmail) throws ResourceNotFoundException, UUIDConversionException, ResourceForbiddenException {
        JobOffer jobOffer = checkJobOfferExists(stringUUID);
        Company company = checkCompanyExists(companyEmail);
        checkCompanyHaveAccessToResource(company, jobOffer);

        jobOfferRepository.delete(jobOffer);
    }

    private JobOffer checkJobOfferExists(String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        return jobOfferRepository.findById(ObjectDtoConverter.stringUUIDToUUID(stringUUID))
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
