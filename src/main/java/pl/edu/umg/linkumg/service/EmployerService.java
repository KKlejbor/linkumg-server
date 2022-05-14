package pl.edu.umg.linkumg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.umg.linkumg.models.Employer;
import pl.edu.umg.linkumg.repositories.EmployerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EmployerService implements IEmployerService {

    @Autowired
    private EmployerRepository employerRepository;

    @Override
    public List<Employer> findAll() {
        return employerRepository.findAll();
    }

    @Override
    public Optional<Employer> findById(int id) {
        return employerRepository.findById(id);
    }

    @Override
    public Employer saveOrUpdate(Employer employer) {
        return employerRepository.save(employer);
    }

    @Override
    public void deleteById(int id) {
        employerRepository.deleteById(id);
    }
}
