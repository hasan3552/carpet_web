package com.company.entity;

import com.company.enums.SaleStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "sale_rug")
public class SaleRugEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "rug_id", nullable = false)
    @ManyToOne(targetEntity = RugEntity.class, fetch = FetchType.LAZY)
    private RugEntity rug;

    @JoinColumn(name = "profile_id", nullable = false)
    @ManyToOne(targetEntity = ProfileEntity.class, fetch = FetchType.LAZY)
    private ProfileEntity profile;

    @Column(nullable = false)
    private Double height;

    @Column(nullable = false, name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SaleStatus status = SaleStatus.ACTIVE;

    @Column(nullable = false)
    private Boolean visible = Boolean.TRUE;

}
