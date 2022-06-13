package pl.edu.umg.linkumg.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name="announcements")
@Data
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer announcementId;
    private String title;
    private Date date;
    private String pathToAnnouncementImage;
    private String content;

    @ManyToOne
    @JoinColumn(name = "college_id", nullable = false)
    private User collegeId;
}
