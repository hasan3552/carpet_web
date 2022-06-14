package com.company.entity;

import com.company.enums.SaleStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "sale_carpet")
public class SaleCarpetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "carpet_id", nullable = false)
    @ManyToOne(targetEntity = CarpetEntity.class,fetch = FetchType.LAZY)
    private CarpetEntity carpet;

    @JoinColumn(name = "profile_id", nullable = false)
    @ManyToOne(targetEntity = ProfileEntity.class,fetch = FetchType.LAZY)
    private ProfileEntity profile;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false, name = "created_date")
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SaleStatus status;

    @Column(nullable = false)
    private Boolean visible = Boolean.TRUE;

}
