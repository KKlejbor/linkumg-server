package pl.edu.umg.linkumg.dtos.resources;

import pl.edu.umg.linkumg.dtos.users.StudentDto;

public record PrivateLessonDto(String lessonId, String title, String category, String location, int pricePerHour,
                               String description, String createdAt, String lastUpdateAt, StudentDto studentDto) {
}
