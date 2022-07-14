package com.company.entity;

import com.company.enums.BasketStatus;
import com.company.enums.ProductType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "basket")
public class BasketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(nullable = false, name = "give_profile")
    @ManyToOne(targetEntity = ProfileEntity.class, fetch = FetchType.LAZY)
    private ProfileEntity giveProfile;

    @JoinColumn(name = "get_profile")
    @ManyToOne(targetEntity = ProfileEntity.class, fetch = FetchType.LAZY)
    private ProfileEntity getProfile;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(nullable = false)
    private ProductType type;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false, name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "returned_date")
    private LocalDateTime returnedDate;

    @Column(nullable = false)
    private String info;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BasketStatus status = BasketStatus.GIVEN;

    @Column(nullable = false)
    private Boolean visible = Boolean.TRUE;

}
