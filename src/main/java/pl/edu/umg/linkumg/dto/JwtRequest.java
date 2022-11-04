package pl.edu.umg.linkumg.dto;

import java.io.Serializable;

public record JwtRequest(String email, String password) implements Serializable {
}
