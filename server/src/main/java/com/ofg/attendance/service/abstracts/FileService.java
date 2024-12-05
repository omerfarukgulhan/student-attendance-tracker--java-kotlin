package com.ofg.attendance.service.abstracts;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface FileService {
    String saveFile(String path, MultipartFile file);

    String saveBase64StringAsFile(String path, String image);

    String encodeImageToBase64(Path filePath);

    void deleteImage(String path, String image);
}
