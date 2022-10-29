package pl.edu.umg.linkumg.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import pl.edu.umg.linkumg.models.Student;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends PagingAndSortingRepository<Student, UUID> {
    Optional<Student> findById(UUID id);

    Student save(Student student);
}
