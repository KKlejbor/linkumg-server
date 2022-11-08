package pl.edu.umg.linkumg.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.edu.umg.linkumg.models.Announcement;

import java.util.UUID;

@Repository
public interface AnnouncementRepository extends PagingAndSortingRepository<Announcement, UUID> {
    Page<Announcement> findAllByTitleOrContentContaining(Pageable pageable, String title, String content);
}
