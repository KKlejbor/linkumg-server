package pl.edu.umg.linkumg.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name="users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_role",
        discriminatorType = DiscriminatorType.INTEGER)
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private boolean active;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String description;

    @Column(name = "user_role", updatable = false, insertable = false)
    private Role userRole;

    private String pathToAvatar;

    public User() {}
}
