package pl.edu.umg.linkumg.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name="private_lessons")
@Data
public class PrivateLesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lessonId;
    private String category;
    private String city;
    private String address;
    private int pricePerHour;
}
