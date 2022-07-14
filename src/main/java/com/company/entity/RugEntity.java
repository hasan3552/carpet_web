package com.company.entity;

import com.company.enums.ProductStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "rug")
@NoArgsConstructor
public class RugEntity {

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
    private Boolean visible = Boolean.TRUE;

    public RugEntity(String uuid) {
        this.uuid = uuid;
    }
}
