package pl.edu.umg.linkumg.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.edu.umg.linkumg.models.PrivateLesson;

import java.util.UUID;

@Repository
public interface PrivateLessonRepository extends PagingAndSortingRepository<PrivateLesson, UUID> {
}
