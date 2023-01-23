package pl.edu.umg.linkumg.models.users;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Admin extends User {

    public Admin(@NonNull String email, @NonNull String password, boolean active,
            boolean banned, @NonNull String name, String description, String pathToAvatar,
            LocalDateTime createdAt, LocalDateTime lastUpdatedAt) {
        super(null, email, password, active, banned, name, description, pathToAvatar, createdAt, lastUpdatedAt);
    }
}
