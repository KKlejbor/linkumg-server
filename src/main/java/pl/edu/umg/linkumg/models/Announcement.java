package pl.edu.umg.linkumg.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="announcements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Announcement {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID announcementId;
    private String title;
    private String pathToAnnouncementImage;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
    private UUID collegeId;
}
