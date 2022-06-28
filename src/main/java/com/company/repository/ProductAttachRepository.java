package com.company.repository;

import com.company.entity.AttachEntity;
import com.company.entity.CarpetEntity;
import com.company.entity.ProductAttachEntity;
import com.company.entity.RugEntity;
import com.company.enums.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductAttachRepository extends JpaRepository<ProductAttachEntity, Integer> {

    Optional<ProductAttachEntity> findByAttachAndTypeAndCarpet(AttachEntity attach, ProductType type, CarpetEntity carpet);

    Optional<ProductAttachEntity> findByAttachAndTypeAndRug(AttachEntity attach, ProductType type, RugEntity rug);
}
