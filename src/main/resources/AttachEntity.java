package com.company.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "attach")
public class AttachEntity {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "uuid", unique = true)
    private String uuid;

    @Column(nullable = false)
    private String originalName;

    @Column(name = "extension", nullable = false)
    private String extension;

    @Column(nullable = false)
    private Long size;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(nullable = false, name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @JoinColumn(name = "article_id")
    @ManyToOne(targetEntity = ArticleEntity.class,fetch = FetchType.LAZY)
    private ArticleEntity article;

    public AttachEntity(String uuid) {
        this.uuid = uuid;
    }
}
