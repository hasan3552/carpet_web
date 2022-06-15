package com.company.entity;

import com.company.enums.ProductAttachStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "carpet_attach")
public class CarpetAttachEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @JoinColumn(nullable = false, name = "carpet_id")
    @ManyToOne(targetEntity = CarpetEntity.class, fetch = FetchType.LAZY)
    private CarpetEntity carpet;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductAttachStatus status;



}


