package com.company.entity;

import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "profile")
@NoArgsConstructor
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProfileStatus status = ProfileStatus.ACTIVE;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProfileRole role = ProfileRole.CUSTOMER;

    @Column(nullable = false)
    private Boolean visible = true;

    @JoinColumn(name = "image_id")
    @OneToOne(fetch = FetchType.LAZY)
    private AttachEntity photo;

    public ProfileEntity(Integer id) {
        this.id = id;
    }
}
