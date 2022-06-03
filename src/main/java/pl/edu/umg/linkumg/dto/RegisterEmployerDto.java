package pl.edu.umg.linkumg.dto;

import lombok.Data;

@Data
public class RegisterEmployerDto {
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String userRole;
    private String companyName;
    private String NIPNumber;
}
