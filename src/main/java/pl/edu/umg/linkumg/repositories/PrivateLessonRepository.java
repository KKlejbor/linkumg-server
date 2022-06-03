package pl.edu.umg.linkumg.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.umg.linkumg.models.PrivateLesson;

import java.util.Date;
import java.util.List;

public interface PrivateLessonRepository extends JpaRepository<PrivateLesson, Integer> {
    List<PrivateLesson> findByPricePerHourBetween(Date from, Date to);
}
