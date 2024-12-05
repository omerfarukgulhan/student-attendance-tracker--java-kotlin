package com.ofg.attendance.repository;

import com.ofg.attendance.model.entity.QRCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface QRCodeRepository extends JpaRepository<QRCode, UUID> {
    Optional<QRCode> findByLectureId(UUID lectureId);
}
