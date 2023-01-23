package pl.edu.umg.linkumg.models.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.umg.linkumg.models.users.College;

import javax.persistence.*;
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
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
    @ManyToOne
    private College college;

    public Announcement(String title, String pathToAnnouncementImage, String description) {
        this.title = title;
        this.pathToAnnouncementImage = pathToAnnouncementImage;
        this.description = description;
    }
}
