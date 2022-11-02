package pl.edu.umg.linkumg.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PrivateLessonDto(UUID lessonId, String title, String category, String city, int pricePerHour,
                               String description, UUID studentId, LocalDateTime createdAt,
                               LocalDateTime lastUpdateAt) {
}
