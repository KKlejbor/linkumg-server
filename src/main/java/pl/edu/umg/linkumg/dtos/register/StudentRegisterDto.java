package pl.edu.umg.linkumg.dtos.register;

import java.io.Serializable;

// TODO: validation faculty is string?
public record StudentRegisterDto(String email, String password, String name, String lastName, Integer indexNumber,
                                 String faculty) implements Serializable {
}
