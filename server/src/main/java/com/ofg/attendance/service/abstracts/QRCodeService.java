package com.ofg.attendance.service.abstracts;

import com.ofg.attendance.model.entity.Lecture;
import com.ofg.attendance.model.entity.QRCode;
import com.ofg.attendance.model.response.QRCodeResponse;

import java.util.UUID;

public interface QRCodeService {
    QRCode generateQRCodeForLecture(Lecture lecture);

    QRCodeResponse getQRCodeByLectureId(UUID lectureId);

    QRCode getQRCodeByContent(String qrCodeContent);

    void deleteQRCodeImage(QRCode qrCode);
}
