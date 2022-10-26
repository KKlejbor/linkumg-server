package pl.edu.umg.linkumg.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import pl.edu.umg.linkumg.models.Announcement;

import java.util.UUID;

public interface AnnouncementRepository extends PagingAndSortingRepository<Announcement, UUID> {

}
