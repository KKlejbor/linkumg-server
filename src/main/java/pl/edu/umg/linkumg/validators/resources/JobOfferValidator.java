package pl.edu.umg.linkumg.validators.resources;

import org.springframework.stereotype.Component;
import pl.edu.umg.linkumg.dtos.resources.save.JobOfferSaveDto;
import pl.edu.umg.linkumg.exceptions.ValidationException;

@Component
public class JobOfferValidator implements ResourceValidator {
    public void validateJobOfferCreate(JobOfferSaveDto jobOfferSaveDto) throws ValidationException {
        validateTitle(jobOfferSaveDto.title());
        validateDescription(jobOfferSaveDto.description());
        validateLocation(jobOfferSaveDto.location());
        validateSalaryUp(jobOfferSaveDto.salaryUp(), jobOfferSaveDto.salaryDown());
        validateSalaryDown(jobOfferSaveDto.salaryDown(), jobOfferSaveDto.salaryUp());
    }

    public void validateSalaryUp(Integer salaryUp, Integer actualSalaryDown) throws ValidationException {
        boolean check = salaryUp.toString().matches("[0-9,. ]{1,8}") && salaryUp >= actualSalaryDown && salaryUp >= 0;
        if (!check) throw new ValidationException("Salary up is invalid.");
    }

    public void validateSalaryDown(Integer salaryDown, Integer actualSalaryUp) throws ValidationException {
        boolean check = salaryDown.toString().matches("[0-9,. ]{1,8}") && salaryDown <= actualSalaryUp && salaryDown >= 0;
        if (!check) throw new ValidationException("Salary down is invalid.");
    }
}
