package pl.edu.umg.linkumg.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AdminDto(String type, UUID id, LocalDateTime createdAt, LocalDateTime lastUpdateAt, String email) {
}
