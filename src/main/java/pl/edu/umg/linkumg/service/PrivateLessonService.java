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
import pl.edu.umg.linkumg.exceptions.ValidationException;
import pl.edu.umg.linkumg.models.resources.PrivateLesson;
import pl.edu.umg.linkumg.models.users.Student;
import pl.edu.umg.linkumg.repositories.PrivateLessonRepository;
import pl.edu.umg.linkumg.repositories.UserRepository;
import pl.edu.umg.linkumg.validators.resources.PrivateLessonValidator;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PrivateLessonService {

    private PrivateLessonRepository privateLessonRepository;
    private UserRepository userRepository;
    private PrivateLessonValidator privateLessonValidator;

    public PrivateLessonDto createNewPrivateLesson(PrivateLessonSaveDto privateLessonSaveDto, String studentEmail) throws ResourceNotFoundException, ValidationException {
        Student student = checkStudentExists(studentEmail);

        privateLessonValidator.validatePrivateLessonCreate(privateLessonSaveDto);

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
    public PrivateLessonDto updatePrivateLesson(String stringUUID, PrivateLessonSaveDto privateLessonSaveDto, String studentEmail) throws ResourceNotFoundException, UUIDConversionException, ResourceForbiddenException, ValidationException {
        PrivateLesson privateLesson = checkPrivateLessonExists(stringUUID);
        Student student = checkStudentExists(studentEmail);
        checkStudentHaveAccessToResource(student, privateLesson);

        int changes = 0;

        if (privateLessonSaveDto.category() != null) {
            privateLessonValidator.validateCategory(privateLessonSaveDto.category());
            privateLesson.setCategory(privateLessonSaveDto.category());
            changes++;
        }
        if (privateLessonSaveDto.location() != null) {
            privateLessonValidator.validateLocation(privateLessonSaveDto.location());
            privateLesson.setLocation(privateLessonSaveDto.location());
            changes++;
        }
        if (privateLessonSaveDto.description() != null) {
            privateLessonValidator.validateDescription(privateLessonSaveDto.description());
            privateLesson.setDescription(privateLessonSaveDto.description());
            changes++;
        }
        if (privateLessonSaveDto.title() != null) {
            privateLessonValidator.validateTitle(privateLessonSaveDto.title());
            privateLesson.setTitle(privateLessonSaveDto.title());
            changes++;
        }
        if (privateLessonSaveDto.pricePerHour() != null) {
            privateLesson.setPricePerHour(privateLessonSaveDto.pricePerHour());
        }

        if (changes > 0) {
            privateLesson.setLastUpdatedAt(LocalDateTime.now());
            privateLessonRepository.save(privateLesson);
        }

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
