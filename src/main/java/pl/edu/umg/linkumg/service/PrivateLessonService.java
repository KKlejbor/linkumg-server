package pl.edu.umg.linkumg.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.umg.linkumg.dtos.resources.PrivateLessonDto;
import pl.edu.umg.linkumg.dtos.resources.save.PrivateLessonSaveDto;
import pl.edu.umg.linkumg.exceptions.ResourceForbiddenException;
import pl.edu.umg.linkumg.exceptions.ResourceNotFoundException;
import pl.edu.umg.linkumg.exceptions.UUIDConversionException;
import pl.edu.umg.linkumg.models.resources.PrivateLesson;
import pl.edu.umg.linkumg.models.users.Student;
import pl.edu.umg.linkumg.repositories.PrivateLessonRepository;
import pl.edu.umg.linkumg.repositories.UserRepository;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PrivateLessonService {

    private PrivateLessonRepository privateLessonRepository;
    private UserRepository userRepository;

    public PrivateLessonDto createNewPrivateLesson(PrivateLessonSaveDto privateLessonSaveDto, String studentEmail) throws ResourceNotFoundException {
        Student student = checkStudentExists(studentEmail);

        // TODO: validate privateLessonDto

        PrivateLesson privateLesson = ObjectDtoConverter.privateLessonSaveDtoToPrivateLessonObject(privateLessonSaveDto);
        privateLesson.setCreatedAt(LocalDateTime.now());
        privateLesson.setLastUpdatedAt(LocalDateTime.now());
        privateLesson.setStudent(student);

        privateLessonRepository.save(privateLesson);

        return ObjectDtoConverter.privateLessonObjectToPrivateLessonDto(privateLesson);
    }

    public PrivateLessonDto findPrivateLessonById(String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        return ObjectDtoConverter.privateLessonObjectToPrivateLessonDto(
                checkPrivateLessonExists(stringUUID)
        );
    }

    public Page<PrivateLessonDto> findPrivateLessonsPage(Pageable pageable, String contain, String category, String location) {
        Page<PrivateLesson> privateLessonPage =
                privateLessonRepository.findAllByTitleContainingOrDescriptionContainingOrCategoryContainingOrLocationContaining(pageable, contain, contain, category, location);

        return privateLessonPage.map(ObjectDtoConverter::privateLessonObjectToPrivateLessonDto);
    }

    @Transactional
    public PrivateLessonDto updatePrivateLesson(String stringUUID, PrivateLessonSaveDto privateLessonSaveDto, String studentEmail) throws ResourceNotFoundException, UUIDConversionException, ResourceForbiddenException {
        PrivateLesson privateLesson = checkPrivateLessonExists(stringUUID);
        Student student = checkStudentExists(studentEmail);
        checkStudentHaveAccessToResource(student, privateLesson);

        // TODO: validation

        if (privateLessonSaveDto.category() != null)
            privateLesson.setCategory(privateLessonSaveDto.category());
        if (privateLessonSaveDto.location() != null)
            privateLesson.setLocation(privateLessonSaveDto.location());
        if (privateLessonSaveDto.description() != null)
            privateLesson.setDescription(privateLessonSaveDto.description());
        if (privateLessonSaveDto.title() != null)
            privateLesson.setTitle(privateLessonSaveDto.title());
        if (privateLessonSaveDto.pricePerHour() > 0)
            privateLesson.setPricePerHour(privateLessonSaveDto.pricePerHour());

        privateLesson.setLastUpdatedAt(LocalDateTime.now());

        privateLessonRepository.save(privateLesson);

        return ObjectDtoConverter.privateLessonObjectToPrivateLessonDto(privateLesson);
    }

    @Transactional
    public void removePrivateLesson(String stringUUID, String studentEmail) throws ResourceNotFoundException, UUIDConversionException, ResourceForbiddenException {
        PrivateLesson privateLesson = checkPrivateLessonExists(stringUUID);
        Student student = checkStudentExists(studentEmail);
        checkStudentHaveAccessToResource(student, privateLesson);

        privateLessonRepository.delete(privateLesson);
    }

    private PrivateLesson checkPrivateLessonExists(String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        return privateLessonRepository.findById(ObjectDtoConverter.stringUUIDToUUID(stringUUID))
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                String.format("Private lesson with %s id not found.", stringUUID)
                        )
                );
    }

    private Student checkStudentExists(String email) throws ResourceNotFoundException {
        return (Student) userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                String.format("User with email %s not found.", email)
                        )
                );
    }

    private void checkStudentHaveAccessToResource(Student student, PrivateLesson privateLesson) throws ResourceForbiddenException {
        if (!privateLesson.getStudent().getUserId().equals(student.getUserId()))
            throw new ResourceForbiddenException(String.format("User with email %s have forbidden access to this resource.", student.getEmail()));
    }
}
