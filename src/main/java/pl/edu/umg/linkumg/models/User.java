package pl.edu.umg.linkumg.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.UUID;

@Entity(name = "users")
@Data
@NoArgsConstructor
public abstract class User implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID userId;
    @Column(unique = true)
    private String email;
    private String password;
    private boolean active;
    private String firstname;
    private String lastname;
    private String description;
    private String userRole;
    private String pathToAvatar;

    public User(UUID userId, String email, String password, boolean active, String firstname, String lastname, String description, String userRole, String pathToAvatar) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.active = active;
        this.firstname = firstname;
        this.lastname = lastname;
        this.description = description;
        this.userRole = userRole;
        this.pathToAvatar = pathToAvatar;
    }
}
