package com.ofg.attendance.service.concretes;

import com.ofg.attendance.config.AppProperties;
import com.ofg.attendance.exception.file.FileServiceException;
import com.ofg.attendance.service.abstracts.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    private final AppProperties appProperties;

    private static final String DEFAULT_EXTENSION = ".png";

    @Autowired
    public FileServiceImpl(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    public String saveFile(String imageType, MultipartFile file) {
        String filename = generateFilename(file.getOriginalFilename());
        Path path = getImagePath(imageType, filename);

        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return filename;
        } catch (IOException e) {
            throw new FileServiceException("save");
        }
    }

    @Override
    public String saveBase64StringAsFile(String imageType, String encodedImage) {
        String filename = UUID.randomUUID() + ".png";
        Path path = getImagePath(imageType, filename);

        try (OutputStream outputStream = Files.newOutputStream(path)) {
            byte[] imageBytes = decodedImage(encodedImage);
            outputStream.write(imageBytes);
            return filename;
        } catch (IOException e) {
            throw new FileServiceException("save64");
        }
    }

    private byte[] decodedImage(String encodedImage) {
        return Base64.getDecoder().decode(encodedImage.split(",")[1]);
    }

    @Override
    public String encodeImageToBase64(Path filePath) {
        byte[] fileBytes = null;
        try {
            fileBytes = Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "data:image/png;base64," + java.util.Base64.getEncoder().encodeToString(fileBytes);
    }

    @Override
    public void deleteImage(String imageType, String image) {
        if (image == null) {
            throw new FileServiceException("image null");
        }
        if ("default.png".equals(image)) {
            return;
        }

        Path path = getImagePath(imageType, image);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new FileServiceException("delete");
        }
    }

    private Path getImagePath(String imageType, String filename) {
        String folder = switch (imageType) {
            case "profile" -> appProperties.getStorage().getProfile();
            case "qr" -> appProperties.getStorage().getQr();
            default -> throw new IllegalArgumentException("Unknown image type: " + imageType);
        };
        return Paths.get(appProperties.getStorage().getRoot(), folder, filename);
    }

    private String generateFilename(String originalFilename) {
        String extension = (originalFilename != null && originalFilename.contains("."))
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : DEFAULT_EXTENSION;
        return UUID.randomUUID() + extension;
    }
}
