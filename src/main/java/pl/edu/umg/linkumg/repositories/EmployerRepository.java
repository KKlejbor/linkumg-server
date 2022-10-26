package pl.edu.umg.linkumg.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import pl.edu.umg.linkumg.models.Employer;

import java.util.UUID;

public interface EmployerRepository extends PagingAndSortingRepository<Employer, UUID> {

}
