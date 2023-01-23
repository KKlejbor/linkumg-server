package pl.edu.umg.linkumg.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.edu.umg.linkumg.models.users.Student;

import java.util.UUID;

@Repository
public interface StudentRepository extends
        PagingAndSortingRepository<Student, UUID> {

    Page<Student> findAllByEmailContaining(Pageable pageable, String email);
}
