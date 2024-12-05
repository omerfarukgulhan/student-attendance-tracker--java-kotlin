package com.ofg.attendance.model.response;

import com.ofg.attendance.model.entity.QRCode;

import java.util.UUID;

public record QRCodeResponse(
        UUID id,
        String qrCodeImagePath,
        String qrCodeContent
) {
    public QRCodeResponse(QRCode qrCode) {
        this(qrCode.getId(), qrCode.getQrCodeImagePath(),
                qrCode.getQrCodeContent());
    }
}
