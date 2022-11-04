package pl.edu.umg.linkumg.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class College extends User {

    private String faculty;
    @OneToMany
    private List<Announcement> announcementList;

    public void addAnnouncement(Announcement announcement){
        announcementList.add(announcement);
    }
}
