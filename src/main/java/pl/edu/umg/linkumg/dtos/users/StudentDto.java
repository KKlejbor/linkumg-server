package pl.edu.umg.linkumg.dtos.users;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record StudentDto(
        String type,
        UUID id,
        String name,
        String lastName,
        LocalDateTime createdAt,
        LocalDateTime lastUpdateAt,
        LocalDate birthDate,
        String phone,
        String email,
        String about,
        SocialDto socials,
        String avatar,
        int indexNumber,
        String faculty) implements Serializable {
}
