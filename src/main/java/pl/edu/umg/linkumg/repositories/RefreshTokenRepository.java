package pl.edu.umg.linkumg.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.edu.umg.linkumg.models.tokens.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);
}
