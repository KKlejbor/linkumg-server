package pl.edu.umg.linkumg.models.users;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;

@Entity
@Data
@NoArgsConstructor
public class College extends User {

    private String faculty;

    public College(@NonNull String email, @NonNull String password, @NonNull String name, String faculty) {
        super(email, password, name);
        this.faculty = faculty;
    }
}
