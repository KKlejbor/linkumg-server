package pl.edu.umg.linkumg.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.edu.umg.linkumg.models.users.College;

import java.util.UUID;

@Repository
public interface CollegeRepository extends
        PagingAndSortingRepository<College, UUID> {

    Page<College> findAllByEmailContaining(Pageable pageable, String email);
}
