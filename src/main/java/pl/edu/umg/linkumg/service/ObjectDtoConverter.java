package pl.edu.umg.linkumg.service;

import org.springframework.stereotype.Component;
import pl.edu.umg.linkumg.dtos.resources.AnnouncementDto;
import pl.edu.umg.linkumg.dtos.resources.JobOfferDto;
import pl.edu.umg.linkumg.dtos.resources.PrivateLessonDto;
import pl.edu.umg.linkumg.dtos.resources.save.AnnouncementSaveDto;
import pl.edu.umg.linkumg.dtos.resources.save.JobOfferSaveDto;
import pl.edu.umg.linkumg.dtos.resources.save.PrivateLessonSaveDto;
import pl.edu.umg.linkumg.dtos.users.*;
import pl.edu.umg.linkumg.exceptions.UUIDConversionException;
import pl.edu.umg.linkumg.models.resources.Announcement;
import pl.edu.umg.linkumg.models.resources.JobOffer;
import pl.edu.umg.linkumg.models.resources.PrivateLesson;
import pl.edu.umg.linkumg.models.users.Admin;
import pl.edu.umg.linkumg.models.users.College;
import pl.edu.umg.linkumg.models.users.Company;
import pl.edu.umg.linkumg.models.users.Student;

import java.util.UUID;

@Component
public class ObjectDtoConverter {

    private ObjectDtoConverter() {
    }

    public static UUID stringUUIDToUUID(String stringUUID) throws UUIDConversionException {
        try {
            return UUID.fromString(stringUUID);
        } catch (IllegalArgumentException exception) {
            throw new UUIDConversionException(String.format("UUID %s isn't correct.", stringUUID));
        }
    }

    public static Announcement announcementSaveDtoToAnnouncementObject(AnnouncementSaveDto announcementSaveDto) {
        return new Announcement(
                announcementSaveDto.title(),
                announcementSaveDto.pathToAnnouncementImage(),
                announcementSaveDto.description()
        );
    }

    public static AnnouncementDto announcementObjectToAnnouncementDto(Announcement announcement) {
        return new AnnouncementDto(
                announcement.getAnnouncementId()
                            .toString(),
                announcement.getTitle(),
                announcement.getPathToAnnouncementImage(),
                announcement.getDescription(),
                announcement.getCreatedAt()
                            .toString(),
                announcement.getLastUpdatedAt()
                            .toString(),
                collegeObjectToCollegeDto(announcement.getCollege())
        );
    }

    public static JobOffer jobOfferSaveDtoToJobOfferObject(JobOfferSaveDto jobOfferSaveDto) {
        return new JobOffer(
                jobOfferSaveDto.title(),
                jobOfferSaveDto.salaryDown(),
                jobOfferSaveDto.salaryUp(),
                jobOfferSaveDto.location(),
                jobOfferSaveDto.description()
        );
    }

    public static JobOfferDto jobOfferObjectToJobOfferDto(JobOffer jobOffer) {
        return new JobOfferDto(
                jobOffer.getJobOfferId()
                        .toString(),
                jobOffer.getTitle(),
                jobOffer.getSalaryDown(),
                jobOffer.getSalaryUp(),
                jobOffer.getLocation(),
                jobOffer.getDescription(),
                jobOffer.getCreatedAt()
                        .toString(),
                jobOffer.getLastUpdatedAt()
                        .toString(),
                companyObjectToCompanyDto(jobOffer.getCompany())
        );
    }

    public static PrivateLesson privateLessonSaveDtoToPrivateLessonObject(PrivateLessonSaveDto privateLessonSaveDto) {
        return new PrivateLesson(
                privateLessonSaveDto.title(),
                privateLessonSaveDto.category(),
                privateLessonSaveDto.location(),
                privateLessonSaveDto.pricePerHour(),
                privateLessonSaveDto.description()
        );
    }

    public static PrivateLessonDto privateLessonObjectToPrivateLessonDto(PrivateLesson privateLesson) {
        return new PrivateLessonDto(
                privateLesson.getLessonId()
                             .toString(),
                privateLesson.getTitle(),
                privateLesson.getCategory(),
                privateLesson.getLocation(),
                privateLesson.getPricePerHour(),
                privateLesson.getDescription(),
                privateLesson.getCreatedAt()
                             .toString(),
                privateLesson.getLastUpdatedAt()
                             .toString(),
                studentObjectToStudentDto(privateLesson.getStudent())
        );
    }

    public static CollegeDto collegeObjectToCollegeDto(College college) {
        return new CollegeDto(
                college.getClass()
                       .getSimpleName()
                       .toLowerCase(),
                college.isBanned(),
                college.getUserId(),
                college.getName(),
                college.getCreatedAt(),
                college.getLastUpdatedAt(),
                college.getEmail(),
                college.getDescription(),
                college.getPathToAvatar()
        );
    }

    public static CompanyDto companyObjectToCompanyDto(Company company) {
        return new CompanyDto(
                company.getClass()
                       .getSimpleName()
                       .toLowerCase(),
                company.isBanned(),
                company.getUserId(),
                company.getName(),
                company.getCreatedAt(),
                company.getLastUpdatedAt(),
                company.getPhoneNumber(),
                company.getEmail(),
                company.getDescription(),
                new SocialDto(
                        company.getSocials()
                               .getGithub(),
                        company.getSocials()
                               .getLinkedin(),
                        company.getSocials()
                               .getFacebook(),
                        company.getSocials()
                               .getInstagram(),
                        company.getSocials()
                               .getOwnSite()
                ),
                company.getPathToAvatar(),
                company.getNip()
        );
    }

    public static StudentDto studentObjectToStudentDto(Student student) {
        return new StudentDto(
                student.getClass()
                       .getSimpleName()
                       .toLowerCase(),
                student.isBanned(),
                student.getUserId(),
                student.getName(),
                student.getLastName(),
                student.getCreatedAt(),
                student.getLastUpdatedAt(),
                student.getBirthYear(),
                student.getPhoneNumber(),
                student.getEmail(),
                student.getDescription(),
                new SocialDto(
                        student.getSocials()
                               .getGithub(),
                        student.getSocials()
                               .getLinkedin(),
                        student.getSocials()
                               .getFacebook(),
                        student.getSocials()
                               .getInstagram(),
                        student.getSocials()
                               .getOwnSite()
                ),
                student.getPathToAvatar(),
                student.getIndexNumber(),
                student.getFaculty()
        );
    }

    public static AdminDto adminObjectToAdminDto(Admin admin) {
        return new AdminDto(
                admin.getClass()
                     .getSimpleName()
                     .toLowerCase(),
                admin.isBanned(),
                admin.getUserId(),
                admin.getName(),
                admin.getCreatedAt(),
                admin.getLastUpdatedAt(),
                admin.getEmail(),
                admin.getDescription(),
                admin.getPathToAvatar()
        );
    }
}
