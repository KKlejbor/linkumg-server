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

@Entity(name="remind_password_tokens")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class RemindPasswordToken extends Token{
    public RemindPasswordToken(User user) {
        super(user, 1);
    }

    public void checkExpired() throws TokenExpiredException {
        super.checkExpired("Password change token expired. Try to generate new, please.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RemindPasswordToken that = (RemindPasswordToken) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
