package pl.edu.umg.linkumg.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.umg.linkumg.dto.PrivateLessonDto;
import pl.edu.umg.linkumg.exception.ResourceForbiddenException;
import pl.edu.umg.linkumg.exception.ResourceNotFoundException;
import pl.edu.umg.linkumg.exception.UUIDConversionException;
import pl.edu.umg.linkumg.models.PrivateLesson;
import pl.edu.umg.linkumg.models.Student;
import pl.edu.umg.linkumg.repositories.PrivateLessonRepository;
import pl.edu.umg.linkumg.repositories.UserRepository;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PrivateLessonService {

    private PrivateLessonRepository privateLessonRepository;
    private UserRepository userRepository;
    private ObjectDtoConverter converter;

    public PrivateLessonDto createNewPrivateLesson(PrivateLessonDto privateLessonDto, String studentEmail) throws ResourceNotFoundException {
        Student student = checkStudentExists(studentEmail);

        // TODO: validate privateLessonDto

        PrivateLesson privateLesson = converter.privateLessonDtoToPrivateLessonObject(privateLessonDto);
        privateLesson.setCreatedAt(LocalDateTime.now());
        privateLesson.setLastUpdatedAt(LocalDateTime.now());
        privateLesson.setStudentId(student.getUserId());
        privateLessonRepository.save(privateLesson);

        student.addPrivateLesson(privateLesson);
        student.setLastUpdatedAt(LocalDateTime.now());
        userRepository.save(student);

        return converter.privateLessonObjectToPrivateLessonDto(privateLesson);
    }

    public PrivateLessonDto findPrivateLessonById(String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        return converter.privateLessonObjectToPrivateLessonDto(
                checkPrivateLessonExists(stringUUID)
        );
    }

    // TODO: findPage
    public void findPrivateLessonsPage(int page) {
    }

    public PrivateLessonDto updatePrivateLesson(String stringUUID, PrivateLessonDto privateLessonDto, String studentEmail) throws ResourceNotFoundException, UUIDConversionException, ResourceForbiddenException {
        PrivateLesson privateLesson = checkPrivateLessonExists(stringUUID);
        Student student = checkStudentExists(studentEmail);
        checkStudentHaveAccessToResource(student, privateLesson);

        // TODO: validation

        if (privateLessonDto.category() != null)
            privateLesson.setCategory(privateLessonDto.category());
        if (privateLessonDto.city() != null)
            privateLesson.setCity(privateLessonDto.city());
        if (privateLessonDto.description() != null)
            privateLesson.setDescription(privateLessonDto.description());
        if (privateLessonDto.title() != null)
            privateLesson.setTitle(privateLessonDto.title());
        if (privateLessonDto.pricePerHour() > 0)
            privateLesson.setPricePerHour(privateLessonDto.pricePerHour());

        privateLesson.setLastUpdatedAt(LocalDateTime.now());

        privateLessonRepository.save(privateLesson);

        return converter.privateLessonObjectToPrivateLessonDto(privateLesson);
    }

    public void removePrivateLesson(String stringUUID, String studentEmail) throws ResourceNotFoundException, UUIDConversionException, ResourceForbiddenException {
        PrivateLesson privateLesson = checkPrivateLessonExists(stringUUID);
        Student student = checkStudentExists(studentEmail);
        checkStudentHaveAccessToResource(student, privateLesson);

        privateLessonRepository.delete(privateLesson);
    }

    private PrivateLesson checkPrivateLessonExists(String stringUUID) throws ResourceNotFoundException, UUIDConversionException {
        return privateLessonRepository.findById(converter.stringUUIDToUUID(stringUUID))
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
        if (!privateLesson.getStudentId().equals(student.getUserId()))
            throw new ResourceForbiddenException(String.format("User with email %s is forbidden to use this resource.", student.getEmail()));
    }
}
