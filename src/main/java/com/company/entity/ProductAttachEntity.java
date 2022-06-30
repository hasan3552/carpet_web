package com.company.entity;

import com.company.enums.AttachStatus;
import com.company.enums.ProductType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "product_attach")
public class ProductAttachEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "product_id")
    @ManyToOne(targetEntity = ProductEntity.class)
    private ProductEntity product;

    @JoinColumn(name = "attach_id")
    @ManyToOne(targetEntity = AttachEntity.class)
    private AttachEntity attach;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AttachStatus status = AttachStatus.ACTIVE;

    @Column(nullable = false, name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean visible = Boolean.TRUE;

}
