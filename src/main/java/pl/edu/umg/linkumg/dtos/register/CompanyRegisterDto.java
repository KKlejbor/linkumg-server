package pl.edu.umg.linkumg.dtos.register;

import java.io.Serializable;

// TODO: validation
public record CompanyRegisterDto(String email, String password, String name, String nip) implements Serializable {
}
