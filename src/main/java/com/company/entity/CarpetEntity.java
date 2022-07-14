package com.company.entity;

import com.company.enums.ProductStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "carpet")
@NoArgsConstructor
@ToString
public class CarpetEntity {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "uuid", unique = true)
    private String uuid;

    @JoinColumn(name = "product_id")
    @ManyToOne(targetEntity = ProductEntity.class)
    private ProductEntity product;

    @Column(nullable = false)
    private Double height;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus status = ProductStatus.ACTIVE;

    @Column(nullable = false, name = "create_date")
    private LocalDateTime createDate = LocalDateTime.now();

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private Boolean visible = Boolean.TRUE;

    public CarpetEntity(String uuid) {
        this.uuid = uuid;
    }
}
