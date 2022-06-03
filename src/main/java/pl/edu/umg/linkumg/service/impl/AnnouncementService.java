package pl.edu.umg.linkumg.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.umg.linkumg.models.Announcement;
import pl.edu.umg.linkumg.repositories.AnnouncementRepository;
import pl.edu.umg.linkumg.service.IAnnouncementService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AnnouncementService implements IAnnouncementService {

    private AnnouncementRepository announcementRepository;

    @Override
    public List<Announcement> findAll() {
        return announcementRepository.findAll();
    }

    @Override
    public Optional<Announcement> findById(int id) {
        return announcementRepository.findById(id);
    }

    @Override
    public Announcement saveOrUpdate(Announcement privateLesson) {
        return announcementRepository.save(privateLesson);
    }

    @Override
    public void deleteById(int id) {
        announcementRepository.deleteById(id);
    }
}
