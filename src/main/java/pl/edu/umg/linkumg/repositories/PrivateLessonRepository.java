package pl.edu.umg.linkumg.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import pl.edu.umg.linkumg.models.PrivateLesson;

import java.util.UUID;

public interface PrivateLessonRepository extends PagingAndSortingRepository<PrivateLesson, UUID> {
}
