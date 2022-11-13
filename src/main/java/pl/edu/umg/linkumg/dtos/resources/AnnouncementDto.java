package pl.edu.umg.linkumg.dtos.resources;

import pl.edu.umg.linkumg.dtos.users.CollegeDto;

public record AnnouncementDto(String announcementId, String title, String pathToAnnouncementImage, String description,
                              String createdAt, String lastUpdatedAt, CollegeDto collegeDto) {
}
