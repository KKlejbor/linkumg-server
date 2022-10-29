package pl.edu.umg.linkumg.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
public class College extends User {

    private String faculty;
    @OneToMany
    private List<Announcement> announcementList;

}
