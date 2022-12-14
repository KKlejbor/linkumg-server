package pl.edu.umg.linkumg.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.edu.umg.linkumg.models.resources.Announcement;

import java.util.UUID;

@Repository
public interface AnnouncementRepository extends PagingAndSortingRepository<Announcement, UUID> {
    Page<Announcement> findAllByTitleContainingAndDescriptionContaining(Pageable pageable, String title, String description);
}
