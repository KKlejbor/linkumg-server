package pl.edu.umg.linkumg.dto;

import lombok.Data;

@Data
public class RegisterStudentDto {
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String userRole;
    private int indexNumber;
    private String faculty;
}
