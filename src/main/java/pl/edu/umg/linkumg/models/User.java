package pl.edu.umg.linkumg.models;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity(name = "users")
@Data
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private boolean active;
    @Column(unique = true)
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String description;
    private String userRole;
    private String pathToAvatar;
}
