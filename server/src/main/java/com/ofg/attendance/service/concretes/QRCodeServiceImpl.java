package com.ofg.attendance.service.concretes;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ofg.attendance.exception.general.NotFoundException;
import com.ofg.attendance.model.entity.Lecture;
import com.ofg.attendance.model.entity.QRCode;
import com.ofg.attendance.model.response.QRCodeResponse;
import com.ofg.attendance.repository.QRCodeRepository;
import com.ofg.attendance.service.abstracts.FileService;
import com.ofg.attendance.service.abstracts.QRCodeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class QRCodeServiceImpl implements QRCodeService {
    private final QRCodeRepository qrCodeRepository;
    private final FileService fileService;

    @Autowired
    public QRCodeServiceImpl(QRCodeRepository qrCodeRepository,
                             FileService fileService) {
        this.qrCodeRepository = qrCodeRepository;
        this.fileService = fileService;
    }

    @Override
    @Transactional
    public QRCode generateQRCodeForLecture(Lecture lecture) {
        String qrCodeContent = generateQRCodeContent(lecture);
        BitMatrix bitMatrix = createQRCodeMatrix(qrCodeContent);
        String qrCodeImagePath = saveQRCodeImage(bitMatrix);
        QRCode qrCode = createQRCodeEntity(lecture, qrCodeContent, qrCodeImagePath);
        return qrCodeRepository.save(qrCode);
    }

    private String generateQRCodeContent(Lecture lecture) {
        return lecture.getId().toString();
    }

    private BitMatrix createQRCodeMatrix(String qrCodeContent) {
        int width = 300;
        int height = 300;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            return qrCodeWriter.encode(qrCodeContent, BarcodeFormat.QR_CODE, width, height);
        } catch (Exception e) {
            throw new RuntimeException("Error generating QR code matrix", e);
        }
    }

    private String saveQRCodeImage(BitMatrix bitMatrix) {
        Path tempFile;
        try {
            tempFile = Files.createTempFile("qr_code_", ".png");
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", tempFile);
            return fileService.saveBase64StringAsFile("qr", fileService.encodeImageToBase64(tempFile));
        } catch (IOException e) {
            throw new RuntimeException("Error saving QR code image", e);
        }
    }

    private QRCode createQRCodeEntity(Lecture lecture, String qrCodeContent, String qrCodeImagePath) {
        QRCode qrCode = new QRCode();
        qrCode.setLecture(lecture);
        qrCode.setQrCodeContent(qrCodeContent);
        qrCode.setQrCodeImagePath(qrCodeImagePath);
        return qrCode;
    }

    @Override
    public QRCodeResponse getQRCodeByLectureId(UUID lectureId) {
        return qrCodeRepository.findByLectureId(lectureId)
                .map(QRCodeResponse::new)
                .orElseThrow(() -> new NotFoundException(lectureId));
    }

    @Override
    public QRCode getQRCodeByContent(String qrCodeContent) {
        return qrCodeRepository.findByQrCodeContent(qrCodeContent)
                .orElseThrow(() -> new NotFoundException(qrCodeContent));
    }

    @Override
    public void deleteQRCodeImage(QRCode qrCode) {
        fileService.deleteImage("qr", qrCode.getQrCodeImagePath());
    }
}