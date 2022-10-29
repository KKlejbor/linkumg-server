package pl.edu.umg.linkumg.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
