package pl.edu.umg.linkumg.dto;

import java.io.Serializable;

public record SocialDto(String github, String linkedin, String facebook, String instagram) implements Serializable {
}
