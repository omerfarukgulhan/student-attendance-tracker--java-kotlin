package com.ofg.attendance.controller;

import com.ofg.attendance.core.util.response.ResponseUtil;
import com.ofg.attendance.core.util.results.ApiDataResponse;
import com.ofg.attendance.model.response.QRCodeResponse;
import com.ofg.attendance.service.abstracts.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/qr-codes")
public class QRCodeController {
    private final QRCodeService qrCodeService;

    private static final String QR_CODE_FETCH_SUCCESS = "app.msg.qr.code.fetch.success";

    @Autowired
    public QRCodeController(QRCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @GetMapping("/{lectureId}")
    public ResponseEntity<ApiDataResponse<QRCodeResponse>> getQRCode(@PathVariable UUID lectureId) {
        QRCodeResponse qrCode = qrCodeService.getQRCodeByLectureId(lectureId);
        return ResponseUtil.createApiDataResponse(qrCode, QR_CODE_FETCH_SUCCESS, HttpStatus.OK);
    }
}
