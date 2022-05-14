package pl.edu.umg.linkumg.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.umg.linkumg.models.Employer;

public interface EmployerRepository extends JpaRepository<Employer, Integer> {

}
