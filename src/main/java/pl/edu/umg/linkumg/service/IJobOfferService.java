package pl.edu.umg.linkumg.service;


import pl.edu.umg.linkumg.models.JobOffer;

import java.util.List;
import java.util.Optional;

public interface IJobOfferService {
    List<JobOffer> findAll();
    Optional<JobOffer> findById(int id);
    JobOffer saveOrUpdate(JobOffer jobOffer);
    void deleteById(int id);
}
