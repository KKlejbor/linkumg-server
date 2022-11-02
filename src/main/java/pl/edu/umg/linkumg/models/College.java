package pl.edu.umg.linkumg.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;

@Entity
@Data
@NoArgsConstructor
public class College extends User {

    private String faculty;
//    @OneToMany
//    private List<Announcement> announcementList;


    public College(@NonNull String email, @NonNull String password) {
        super(email, password);
    }
}
