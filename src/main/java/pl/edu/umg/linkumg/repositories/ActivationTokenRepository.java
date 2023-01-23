package pl.edu.umg.linkumg.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.edu.umg.linkumg.models.tokens.ActivationToken;

import java.util.Optional;
import java.util.UUID;

public interface ActivationTokenRepository extends CrudRepository<ActivationToken, UUID> {
    Optional<ActivationToken> findByToken(String token);
}
