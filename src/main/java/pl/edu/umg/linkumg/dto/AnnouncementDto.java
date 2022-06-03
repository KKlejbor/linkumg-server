package pl.edu.umg.linkumg.dto;

import lombok.Data;

@Data
public class AnnouncementDto {
    private Integer announcementId;
    private String title;
    private String pathToAnnouncementImage;
    private String content;
    private String userId;
}
