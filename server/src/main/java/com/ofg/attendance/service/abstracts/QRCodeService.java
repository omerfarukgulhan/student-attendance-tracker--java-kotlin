package com.ofg.attendance.service.abstracts;

import com.ofg.attendance.model.entity.Lecture;
import com.ofg.attendance.model.entity.QRCode;

import java.util.UUID;

public interface QRCodeService {
    QRCode generateQRCodeForLecture(Lecture lecture);

    QRCode getQRCodeByLectureId(UUID lectureId);

    void deleteQRCodeImage(QRCode qrCode);
}
