package pl.edu.umg.linkumg.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.edu.umg.linkumg.models.resources.PrivateLesson;

import java.util.UUID;

@Repository
public interface PrivateLessonRepository extends PagingAndSortingRepository<PrivateLesson, UUID> {

    Page<PrivateLesson> findAllByTitleContainingAndCategoryContainingAndLocationContainingAllIgnoreCase(
            Pageable pageable, String title, String category, String location);
}
