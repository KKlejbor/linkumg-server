package pl.edu.umg.linkumg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.umg.linkumg.models.Student;
import pl.edu.umg.linkumg.repositories.StudentRepository;

import java.util.UUID;

@Service
public class StudentService{

    private StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // TODO: maybe should use user services and extend it when need
//    public List<Student> findAll() {
//        return studentRepository.findAll();
//    }

    public Student findById(UUID id) {
        return studentRepository.findById(id).get();
    }

    public Student save(Student student) {
        return studentRepository.save(student).get();
    }

    public Student update(){
        return null;
    }

    public void deleteById(UUID id) {

    }
}
