package com.company.entity;

import com.company.enums.ProductType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "uuid", unique = true)
    private String uuid;

    @JoinColumn(name = "factory_id", nullable = false)
    @ManyToOne(targetEntity = FactoryEntity.class, fetch = FetchType.LAZY)
    private FactoryEntity factory;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String design;

    @Column(nullable = false)
    private String colour;

    @Column(nullable = false)
    private String pon;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Column(nullable = false, name = "create_date")
    private LocalDateTime createDate = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean visible = Boolean.TRUE;
}
