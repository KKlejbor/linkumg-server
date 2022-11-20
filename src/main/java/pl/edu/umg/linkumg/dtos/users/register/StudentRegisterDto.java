package pl.edu.umg.linkumg.dtos.users.register;

import java.io.Serializable;

public record StudentRegisterDto(String email, String password, String name, String lastName, Integer indexNumber,
                                 String faculty) implements Serializable {
}
