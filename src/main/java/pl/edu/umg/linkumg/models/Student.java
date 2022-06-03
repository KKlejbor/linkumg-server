package pl.edu.umg.linkumg.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Student extends User {
    @Column(unique = true)
    private int indexNumber;
    private String faculty;
    @OneToMany
    private List<PrivateLesson> privateLessonList;
}
