package pl.edu.umg.linkumg.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.umg.linkumg.models.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

}
