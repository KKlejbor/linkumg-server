package pl.edu.umg.linkumg.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import pl.edu.umg.linkumg.models.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, UUID> {
    Optional<User> findById(@NonNull UUID id);

    Optional<User> findByEmail(String email);
}
