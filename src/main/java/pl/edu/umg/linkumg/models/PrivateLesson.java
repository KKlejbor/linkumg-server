package pl.edu.umg.linkumg.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.UUID;

@Entity(name="private_lessons")
@Data
public class PrivateLesson {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID lessonId;
    private String category;
    private String city;
    private String address;
    private int pricePerHour;
    @ManyToOne
    private Student student;
}
