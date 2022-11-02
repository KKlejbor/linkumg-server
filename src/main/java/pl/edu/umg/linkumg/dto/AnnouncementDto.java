package pl.edu.umg.linkumg.dto;

public record AnnouncementDto(String id, String title, String pathToAnnouncementImage, String content, String createdAt,
                              String lastUpdatedAt, String collegeId) {
}
