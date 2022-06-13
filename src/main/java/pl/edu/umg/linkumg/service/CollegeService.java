package pl.edu.umg.linkumg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.umg.linkumg.models.College;
import pl.edu.umg.linkumg.repositories.CollegeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CollegeService implements ICollegeService {

    @Autowired
    private CollegeRepository collegeRepository;

    @Override
    public List<College> findAll() {
        return collegeRepository.findAll();
    }

    @Override
    public Optional<College> findById(int id) {
        return collegeRepository.findById(id);
    }

    @Override
    public College saveOrUpdate(College college) {
        return collegeRepository.save(college);
    }

    @Override
    public void deleteById(int id) {
        collegeRepository.deleteById(id);
    }
}
