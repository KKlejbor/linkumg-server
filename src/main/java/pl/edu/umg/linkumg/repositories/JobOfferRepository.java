package pl.edu.umg.linkumg.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.umg.linkumg.models.JobOffer;

public interface JobOfferRepository extends JpaRepository<JobOffer, Integer> {

}
