package pl.edu.umg.linkumg.service;

import pl.edu.umg.linkumg.models.Announcement;

import java.util.List;
import java.util.Optional;

public interface IAnnouncementService {
    List<Announcement> findAll();
    Optional<Announcement> findById(int id);
    Announcement saveOrUpdate(Announcement privateLesson);
    void deleteById(int id);
}
