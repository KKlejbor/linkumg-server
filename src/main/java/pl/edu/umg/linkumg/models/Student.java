package pl.edu.umg.linkumg.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Student extends User {
    private int indexNumber;
    private String faculty;
    @OneToMany
    private List<PrivateLesson> privateLessonList;

    public Student(UUID userId, String email, String password, boolean active, String firstname, String lastname, String description, String userRole, String pathToAvatar, int indexNumber, String faculty) {
        super(userId, email, password, active, firstname, lastname, description, userRole, pathToAvatar);
        this.indexNumber = indexNumber;
        this.faculty = faculty;
    }
}
