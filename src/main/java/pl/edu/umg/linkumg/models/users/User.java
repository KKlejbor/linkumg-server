package pl.edu.umg.linkumg.models.users;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "users")
@Table(indexes = {
        @Index(name = "email_index", columnList = "email")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public abstract class User{
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID userId;
    @Column(unique = true)
    @NonNull
    private String email;
    @NonNull
    private String password;
    private boolean active;
    private boolean banned;
    @NonNull
    private String name;
    private String description;
    private String pathToAvatar;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
}
