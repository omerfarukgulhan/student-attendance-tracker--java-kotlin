package com.ofg.attendance.model.entity;

import com.ofg.attendance.core.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "qr_codes")
@Data
public class QRCode extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "UUID")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "lecture_id", unique = true)
    private Lecture lecture;

    private String qrCodeImagePath;

    private String qrCodeContent;
}