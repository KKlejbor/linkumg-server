package pl.edu.umg.linkumg.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.edu.umg.linkumg.models.resources.JobOffer;

import java.util.UUID;

@Repository
public interface JobOfferRepository extends PagingAndSortingRepository<JobOffer, UUID> {
    Page<JobOffer> findAllByTitleContainingOrDescriptionContainingOrLocationContaining(Pageable pageable, String title, String description, String location);

    Page<JobOffer> findAllByTitleContainingOrDescriptionContainingOrLocationContainingAndSalaryDownIsGreaterThanEqualAndSalaryUpIsLessThanEqual(Pageable pageable, String title, String description, String location, int salaryDown, int salaryUp);

    Page<JobOffer> findAllByTitleContainingOrDescriptionContainingOrLocationContainingAndSalaryDownIsGreaterThanEqual(Pageable pageable, String title, String description, String location, int salaryDown);

    Page<JobOffer> findAllByTitleContainingOrDescriptionContainingOrLocationContainingAndSalaryUpIsLessThanEqual(Pageable pageable, String title, String description, String location, int salaryUp);
}
