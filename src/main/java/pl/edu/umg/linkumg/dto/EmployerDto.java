package pl.edu.umg.linkumg.dto;

import lombok.Getter;
import lombok.Setter;
import pl.edu.umg.linkumg.models.Role;

public class EmployerDto {

    @Getter @Setter
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

    @Getter @Setter
    private Role userRole;

    @Getter @Setter
    private String pathToAvatar;

    @Getter @Setter
    private String companyName;

    @Getter @Setter
    private String leiNumber;
}
