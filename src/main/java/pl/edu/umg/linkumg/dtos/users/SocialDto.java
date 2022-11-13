package pl.edu.umg.linkumg.dtos.users;

import java.io.Serializable;

public record SocialDto(String github, String linkedin, String facebook, String instagram) implements Serializable {
}
