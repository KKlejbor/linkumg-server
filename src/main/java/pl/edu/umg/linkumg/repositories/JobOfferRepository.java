package pl.edu.umg.linkumg.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.edu.umg.linkumg.models.resources.JobOffer;

import java.util.UUID;

@Repository
public interface JobOfferRepository extends PagingAndSortingRepository<JobOffer, UUID> {

    Page<JobOffer> findAllByCompanyBannedFalseAndTitleContainingAndLocationContainingAllIgnoreCase(Pageable pageable,
            String title, String location);

    Page<JobOffer> findAllByCompanyBannedFalseAndTitleContainingAndLocationContainingAndSalaryDownIsGreaterThanEqualAndSalaryUpIsLessThanEqualAllIgnoreCase(
            Pageable pageable, String title, String location, int salaryDown, int salaryUp);

    Page<JobOffer> findAllByCompanyBannedFalseAndTitleContainingAndLocationContainingAndSalaryDownIsGreaterThanEqualAllIgnoreCase(
            Pageable pageable, String title, String location, int salaryDown);

    Page<JobOffer> findAllByCompanyBannedFalseAndTitleContainingAndLocationContainingAndSalaryUpIsLessThanEqualAllIgnoreCase(
            Pageable pageable, String title, String location, int salaryUp);
}
