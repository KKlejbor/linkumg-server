package pl.edu.umg.linkumg.dtos.users;

import java.time.LocalDateTime;
import java.util.UUID;

public record AdminDto(String type, boolean banned, UUID id, String name, LocalDateTime createdAt,
                       LocalDateTime lastUpdateAt, String email, String about, String avatar) {

}
