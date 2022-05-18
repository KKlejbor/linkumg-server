package pl.edu.umg.linkumg.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_role",
        discriminatorType = DiscriminatorType.INTEGER)
public class User {

    @Id
    @Getter @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Getter @Setter
    private boolean active;

    @Getter @Setter
    private String email;

    @Getter @Setter
    private String password;

    @Getter @Setter
    private String firstname;

    @Getter @Setter
    private String lastname;

    @Getter @Setter
    private String description;

//    @Enumerated(EnumType.ORDINAL)
    @Getter @Setter
    @Column(name = "user_role", updatable = false, insertable = false)
    private Role userRole;

    @Getter @Setter
    private String pathToAvatar;

    public User() {}
}
