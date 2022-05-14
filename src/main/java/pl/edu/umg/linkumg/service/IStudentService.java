package pl.edu.umg.linkumg.service;

import pl.edu.umg.linkumg.models.Student;

import java.util.List;
import java.util.Optional;

public interface IStudentService {
    List<Student> findAll();
    Optional<Student> findById(int id);
    Student saveOrUpdate(Student student);
    void deleteById(int id);
}
