package pl.edu.umg.linkumg.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.umg.linkumg.models.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

}
