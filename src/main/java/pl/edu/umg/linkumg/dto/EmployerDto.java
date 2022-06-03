package pl.edu.umg.linkumg.dto;

import lombok.Getter;
import lombok.Setter;
import pl.edu.umg.linkumg.models.Role;

@Getter
@Setter
public class EmployerDto {
    private Integer userId;
    private boolean active;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String description;
    private Role userRole;
    private String pathToAvatar;
    private String companyName;
    private String leiNumber;
}
