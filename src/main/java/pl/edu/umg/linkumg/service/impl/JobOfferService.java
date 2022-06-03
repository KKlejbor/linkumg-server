package pl.edu.umg.linkumg.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.umg.linkumg.models.JobOffer;
import pl.edu.umg.linkumg.repositories.JobOfferRepository;
import pl.edu.umg.linkumg.service.IJobOfferService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class JobOfferService implements IJobOfferService {

    JobOfferRepository jobOfferRepository;

    @Override
    public List<JobOffer> findAll() {
        return jobOfferRepository.findAll();
    }

    @Override
    public Optional<JobOffer> findById(int id) {
        return jobOfferRepository.findById(id);
    }

    @Override
    public JobOffer saveOrUpdate(JobOffer jobOffer) {
        return jobOfferRepository.save(jobOffer);
    }

    @Override
    public void deleteById(int id) {
        jobOfferRepository.deleteById(id);
    }
}
