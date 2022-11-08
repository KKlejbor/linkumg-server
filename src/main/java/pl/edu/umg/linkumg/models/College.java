package pl.edu.umg.linkumg.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class College extends User {

    private String faculty;
    @OneToMany
    private List<Announcement> announcementList = new ArrayList<>();

    public College(UUID userId, @NonNull String email, @NonNull String password, boolean active, boolean banned, @NonNull String name, String description, String pathToAvatar, LocalDateTime createdAt, LocalDateTime lastUpdatedAt, String faculty) {
        super(userId, email, password, active, banned, name, description, pathToAvatar, createdAt, lastUpdatedAt);
        this.faculty = faculty;
    }

    public void addAnnouncement(Announcement announcement){
        announcementList.add(announcement);
    }
}
