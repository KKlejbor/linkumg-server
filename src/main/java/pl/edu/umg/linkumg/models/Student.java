package pl.edu.umg.linkumg.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student extends User {
    private String lastName;
    private LocalDate birthYear;
    private String phoneNumber;
    @OneToOne(cascade = CascadeType.ALL)
    private Social socials;
    private int indexNumber;
    private String faculty;
    @OneToMany(cascade = CascadeType.ALL)
    private List<PrivateLesson> privateLessonList;

    public Student(@NonNull String email, @NonNull String password) {
        super(email, password);
    }

    public Student(UUID userId, @NonNull String email, @NonNull String password, boolean active, String name, String description, String pathToAvatar, LocalDateTime createdAt, LocalDateTime lastUpdateAt, String lastName, LocalDate birthYear, String phoneNumber, Social socials, int indexNumber, String faculty) {
        super(userId, email, password, active, name, description, pathToAvatar, createdAt, lastUpdateAt);
        this.lastName = lastName;
        this.birthYear = birthYear;
        this.phoneNumber = phoneNumber;
        this.socials = socials;
        this.indexNumber = indexNumber;
        this.faculty = faculty;
    }

    public void addPrivateLesson(PrivateLesson privateLesson){
        privateLessonList.add(privateLesson);
    }
}
