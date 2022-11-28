package pl.edu.umg.linkumg.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.umg.linkumg.exceptions.UUIDConversionException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageService {

    private static final String GLOBAL_PATH = "../images/";
    private static final String USERS_IMAGES_PATH = "users/";
    private static final String ANNOUNCEMENTS_IMAGES_PATH = "announcements/";

    public String saveUserImage(String stringUUID, MultipartFile image) throws IOException, UUIDConversionException {
        return saveImage(stringUUID, image, USERS_IMAGES_PATH);
    }

    public String saveAnnouncementImage(String stringUUID, MultipartFile image) throws IOException, UUIDConversionException {
        return saveImage(stringUUID, image, ANNOUNCEMENTS_IMAGES_PATH);
    }

    public InputStream getUserImage(String fileName) throws IOException {
        return getImage(fileName, USERS_IMAGES_PATH);
    }

    public InputStream getAnnouncementImage(String fileName) throws IOException {
        return getImage(fileName, ANNOUNCEMENTS_IMAGES_PATH);
    }

    private String saveImage(String stringUUID, MultipartFile image, String folderPath) throws IOException, UUIDConversionException {
        UUID uuid = ObjectDtoConverter.stringUUIDToUUID(stringUUID);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(uuid.hashCode() & 0x7fffffff);

        Optional<String> extension = getExtensionByStringHandling(image.getOriginalFilename());

        if (extension.isPresent()) {
            stringBuilder.append(".");
            stringBuilder.append(extension.get());
        }

        String fileName = stringBuilder.toString();

        Path path = Path.of(GLOBAL_PATH + folderPath + fileName);
        Files.write(path, image.getBytes());

        return fileName;
    }

    private InputStream getImage(String fileName, String folderPath) throws IOException {
        String path = GLOBAL_PATH + folderPath + fileName;

        return new BufferedInputStream(new FileInputStream(path));
    }

    private Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
}
