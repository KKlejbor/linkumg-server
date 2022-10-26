package pl.edu.umg.linkumg.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import pl.edu.umg.linkumg.models.College;

import java.util.UUID;

public interface CollegeRepository extends PagingAndSortingRepository<College, UUID> {

}
