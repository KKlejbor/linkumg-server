package pl.edu.umg.linkumg.dtos.users.register;

import java.io.Serializable;

public record CompanyRegisterDto(String email, String password, String name, String nip) implements Serializable {
}
