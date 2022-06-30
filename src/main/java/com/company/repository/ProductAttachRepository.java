package com.company.repository;

import com.company.entity.*;
import com.company.enums.AttachStatus;
import com.company.enums.ProductStatus;
import com.company.enums.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductAttachRepository extends JpaRepository<ProductAttachEntity, Integer> {

    List<ProductAttachEntity> findAllByProductAndStatusAndVisible
            (ProductEntity product, AttachStatus status, Boolean visible);

}
