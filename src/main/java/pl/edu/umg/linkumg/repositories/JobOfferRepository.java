package pl.edu.umg.linkumg.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import pl.edu.umg.linkumg.models.JobOffer;

import java.util.UUID;

public interface JobOfferRepository extends PagingAndSortingRepository<JobOffer, UUID> {

}
