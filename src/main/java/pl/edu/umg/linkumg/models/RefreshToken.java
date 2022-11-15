package pl.edu.umg.linkumg.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.umg.linkumg.exceptions.TokenExpiredException;
import pl.edu.umg.linkumg.models.users.User;

import javax.persistence.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity(name="refresh_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    @Column(unique = true)
    private String token;
    private Instant expiresAt;
    @OneToOne
    private User user;

    public RefreshToken(User user) {
        this.token = UUID.randomUUID().toString();
        this.expiresAt = Instant.now().plus(24, ChronoUnit.HOURS);
        this.user = user;
    }

    public void checkExpired() throws TokenExpiredException {
        if(Instant.now().isAfter(expiresAt))
            throw new TokenExpiredException("Refresh token expired. Sign in with credentials, please.");
    }
}
