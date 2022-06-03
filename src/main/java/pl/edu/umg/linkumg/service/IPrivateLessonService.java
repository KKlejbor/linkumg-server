package pl.edu.umg.linkumg.service;

import pl.edu.umg.linkumg.models.PrivateLesson;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IPrivateLessonService {
    List<PrivateLesson> findAll();
    Optional<PrivateLesson> findById(int id);
    PrivateLesson saveOrUpdate(PrivateLesson privateLesson);
    List<PrivateLesson> findByPricePerHourBetween(Date from, Date to);
    void deleteById(int id);
}
