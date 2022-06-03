package pl.edu.umg.linkumg.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.umg.linkumg.models.PrivateLesson;
import pl.edu.umg.linkumg.repositories.PrivateLessonRepository;
import pl.edu.umg.linkumg.service.IPrivateLessonService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PrivateLessonService implements IPrivateLessonService {

    private PrivateLessonRepository privateLessonRepository;

    @Override
    public List<PrivateLesson> findAll() {
        return privateLessonRepository.findAll();
    }

    @Override
    public Optional<PrivateLesson> findById(int id) {
        return privateLessonRepository.findById(id);
    }

    @Override
    public PrivateLesson saveOrUpdate(PrivateLesson privateLesson) {
        return privateLessonRepository.save(privateLesson);
    }

    @Override
    public List<PrivateLesson> findByPricePerHourBetween(Date from, Date to) {
        return privateLessonRepository.findByPricePerHourBetween(from, to);
    }

    @Override
    public void deleteById(int id) {
        privateLessonRepository.deleteById(id);
    }
}
