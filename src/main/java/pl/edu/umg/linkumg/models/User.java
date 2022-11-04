package pl.edu.umg.linkumg.models;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "users")
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
