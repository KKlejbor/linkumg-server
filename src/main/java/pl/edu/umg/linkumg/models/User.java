package pl.edu.umg.linkumg.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity(name = "users")
@Data
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
    private String userRole;
    private String pathToAvatar;
}
