package pl.edu.umg.linkumg.dtos;

import java.io.Serializable;

public record LoginDto(String email, String password) implements Serializable {
}
