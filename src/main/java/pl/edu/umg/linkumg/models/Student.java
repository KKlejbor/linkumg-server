package pl.edu.umg.linkumg.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public Student(@NonNull String email, @NonNull String password, @NonNull String name, String lastName, int indexNumber, String faculty) {
        super(email, password, name);
        this.lastName = lastName;
        this.indexNumber = indexNumber;
        this.faculty = faculty;
    }

    public Student(UUID userId, @NonNull String email, @NonNull String password, boolean active, boolean banned, @NonNull String name, String description, String pathToAvatar, LocalDateTime createdAt, LocalDateTime lastUpdatedAt, String lastName, LocalDate birthYear, String phoneNumber, Social socials, int indexNumber, String faculty) {
        super(userId, email, password, active, banned, name, description, pathToAvatar, createdAt, lastUpdatedAt);
        this.lastName = lastName;
        this.birthYear = birthYear;
        this.phoneNumber = phoneNumber;
        this.socials = socials;
        this.indexNumber = indexNumber;
        this.faculty = faculty;
        this.privateLessonList = new ArrayList<>();
    }

    public void addPrivateLesson(PrivateLesson privateLesson){
        privateLessonList.add(privateLesson);
    }
}
