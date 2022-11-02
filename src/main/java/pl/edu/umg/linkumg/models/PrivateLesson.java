package pl.edu.umg.linkumg.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
    private String city;
    private int pricePerHour;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
    private UUID studentId;
}
