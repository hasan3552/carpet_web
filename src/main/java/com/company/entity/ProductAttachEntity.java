package com.company.entity;

import com.company.enums.AttachStatus;
import com.company.enums.ProductType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "product_attach")
public class ProductAttachEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType type;

    @JoinColumn(name = "carpet_id")
    @ManyToOne(targetEntity = CarpetEntity.class)
    private CarpetEntity carpet;

    @JoinColumn(name = "rug_id")
    @ManyToOne(targetEntity = RugEntity.class)
    private RugEntity rug;

    @JoinColumn(name = "attach_id")
    @ManyToOne(targetEntity = AttachEntity.class)
    private AttachEntity attach;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AttachStatus status;

    @Column(nullable = false)
    private Boolean visible = Boolean.TRUE;

}
