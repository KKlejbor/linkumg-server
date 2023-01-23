package pl.edu.umg.linkumg.models.tokens;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import pl.edu.umg.linkumg.exceptions.TokenExpiredException;
import pl.edu.umg.linkumg.models.users.User;

import javax.persistence.Entity;
import java.util.Objects;

@Entity(name="activation_tokens")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ActivationToken extends Token{
    public ActivationToken(User user) {
        super(user, 3);
    }

    public void checkExpired() throws TokenExpiredException {
        super.checkExpired("Activation token expired.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ActivationToken that = (ActivationToken) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
