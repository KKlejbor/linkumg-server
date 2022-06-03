package pl.edu.umg.linkumg.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.umg.linkumg.models.Announcement;

public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {

}
