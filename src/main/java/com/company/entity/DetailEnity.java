package com.company.entity;

import com.company.enums.DetailStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "detail")
public class DetailEnity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(nullable = false)
    @ManyToOne(targetEntity = ProfileEntity.class, fetch = FetchType.LAZY)
    private ProfileEntity profile;

    @Column(nullable = false, name = "create_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DetailStatus status = DetailStatus.ACTIVE;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private Boolean visible = Boolean.TRUE;

}
