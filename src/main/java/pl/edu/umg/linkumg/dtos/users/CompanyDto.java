package pl.edu.umg.linkumg.dtos.users;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public record CompanyDto(String type, boolean active, UUID id, String name, LocalDateTime createdAt,
                         LocalDateTime lastUpdateAt, String phone, String email, String about, SocialDto socials,
                         String avatar, String nip) implements Serializable {
}
