package com.company.repository;

import com.company.entity.CarpetEntity;
import com.company.entity.FactoryEntity;
import com.company.entity.ProductEntity;
import com.company.entity.RugEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface RugRepository extends PagingAndSortingRepository<RugEntity, String> {

    Optional<RugEntity> findByProductAndWeight(ProductEntity product, Double weight);
}
