package pl.edu.umg.linkumg.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import pl.edu.umg.linkumg.models.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends PagingAndSortingRepository<User, UUID> {
    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);
}
