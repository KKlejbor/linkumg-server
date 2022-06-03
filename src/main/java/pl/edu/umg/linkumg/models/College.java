package pl.edu.umg.linkumg.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class College extends User {
    private String faculty;
    @OneToMany(mappedBy = "announcements")
    private List<Announcement> announcements;
}
