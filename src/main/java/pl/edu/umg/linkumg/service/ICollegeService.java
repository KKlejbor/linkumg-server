package pl.edu.umg.linkumg.service;

import pl.edu.umg.linkumg.models.College;

import java.util.List;
import java.util.Optional;

public interface ICollegeService {
    List<College> findAll();
    Optional<College> findById(int id);
    College saveOrUpdate(College college);
    void deleteById(int id);
}
