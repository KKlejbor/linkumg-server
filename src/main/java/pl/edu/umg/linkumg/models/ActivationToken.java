package pl.edu.umg.linkumg.models;

import lombok.*;
import pl.edu.umg.linkumg.models.users.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity(name="activation_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivationToken {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID uuid;
    private String token;
    private Instant expiresAt;
    @OneToOne
    private User user;

    public ActivationToken(String token, User user) {
        this.token = token;
        this.expiresAt = Instant.now().plus(3, ChronoUnit.HOURS);
        this.user = user;
    }
}
