package pl.edu.umg.linkumg.dto;

import lombok.Getter;
import lombok.Setter;
import pl.edu.umg.linkumg.models.Student;

@Getter
@Setter
public class PrivateLessonDto {

    private Integer lessonId;

    private String city;
    private String address;
    private int pricePerHour;
    private String category;

    private Student studentId;
}
