package pl.edu.umg.linkumg.models.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import pl.edu.umg.linkumg.models.Social;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student extends User {
    private String lastName;
    private LocalDate birthYear;
    private String phoneNumber;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "social_uuid")
    private Social socials;
    private int indexNumber;
    private String faculty;

    public Student(@NonNull String email, @NonNull String password, @NonNull String name, String lastName, int indexNumber, String faculty) {
        super(email, password, name);
        this.lastName = lastName;
        this.indexNumber = indexNumber;
        this.faculty = faculty;
    }
}
