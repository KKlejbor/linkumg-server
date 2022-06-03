package pl.edu.umg.linkumg.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.umg.linkumg.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findAll();

    Optional<User> findById(int id);

    Optional<User> findByEmailAndPassword(String email, String password);
}
