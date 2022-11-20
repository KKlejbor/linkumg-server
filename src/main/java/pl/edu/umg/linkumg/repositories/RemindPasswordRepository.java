package pl.edu.umg.linkumg.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.edu.umg.linkumg.models.tokens.RemindPasswordToken;

import java.util.Optional;
import java.util.UUID;

public interface RemindPasswordRepository  extends CrudRepository<RemindPasswordToken, UUID> {
    Optional<RemindPasswordToken> findByToken(String token);
}
