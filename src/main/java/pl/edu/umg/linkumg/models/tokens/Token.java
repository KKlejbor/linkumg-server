package pl.edu.umg.linkumg.models.tokens;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import pl.edu.umg.linkumg.exceptions.TokenExpiredException;
import pl.edu.umg.linkumg.models.users.User;

import javax.persistence.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
@ToString
public abstract class Token {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    @Column(unique = true)
    private String token;
    private Instant expiresAt;
    @OneToOne
    private User user;

    protected Token() {
    }

    protected Token(User user, int hours) {
        this.token = UUID.randomUUID().toString();
        this.expiresAt = Instant.now().plus(hours, ChronoUnit.HOURS);
        this.user = user;
    }

    public void checkExpired(String message) throws TokenExpiredException {
        if(Instant.now().isAfter(expiresAt))
            throw new TokenExpiredException(message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Token token = (Token) o;
        return id != null && Objects.equals(id, token.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
