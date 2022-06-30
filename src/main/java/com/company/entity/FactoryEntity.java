package com.company.entity;

import com.company.enums.FactoryStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "factory")
@NoArgsConstructor
public class FactoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String key = "factory_"+name;

    @Column(nullable = false, name = "create_date")
    private LocalDateTime createdDate = LocalDateTime.now();


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FactoryStatus status = FactoryStatus.ACTIVE;

    @Column(nullable = false)
    private Boolean visible = Boolean.TRUE;

    @JoinColumn(name = "image_id")
    @OneToOne(fetch = FetchType.LAZY)
    private AttachEntity attach;

    public FactoryEntity(Integer id) {
        this.id = id;
    }
}
