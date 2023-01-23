package pl.edu.umg.linkumg.models.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.umg.linkumg.models.users.Student;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name="private_lessons")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrivateLesson {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID lessonId;
    private String title;
    private String category;
    private String location;
    private int pricePerHour;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
    @ManyToOne
    private Student student;

    public PrivateLesson(String title, String category, String location, int pricePerHour, String description) {
        this.title = title;
        this.category = category;
        this.location = location;
        this.pricePerHour = pricePerHour;
        this.description = description;
    }
}
