package pl.edu.umg.linkumg.service;

import pl.edu.umg.linkumg.models.Employer;

import java.util.List;
import java.util.Optional;

public interface IEmployerService {
    List<Employer> findAll();
    Optional<Employer> findById(int id);
    Employer saveOrUpdate(Employer employer);
    void deleteById(int id);
}
