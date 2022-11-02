package pl.edu.umg.linkumg.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.edu.umg.linkumg.models.JobOffer;

import java.util.UUID;

@Repository
public interface JobOfferRepository extends PagingAndSortingRepository<JobOffer, UUID> {
}
