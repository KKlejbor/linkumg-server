package pl.edu.umg.linkumg.dto;

import java.io.Serializable;

// TODO: validation
public record CompanyRegisterDto(String email, String password, String name, String nip) implements Serializable {
}
