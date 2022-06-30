package com.company.repository;

import com.company.entity.CarpetEntity;
import com.company.entity.FactoryEntity;
import com.company.entity.ProductEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface CarpetRepository extends PagingAndSortingRepository<CarpetEntity, String> {

    Optional<CarpetEntity> findByProductAndWeightAndHeight(ProductEntity product, Double weight, Double height);
}
