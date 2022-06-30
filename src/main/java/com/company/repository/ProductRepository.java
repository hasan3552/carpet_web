package com.company.repository;

import com.company.entity.FactoryEntity;
import com.company.entity.ProductEntity;
import com.company.enums.ProductType;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ProductRepository extends PagingAndSortingRepository<ProductEntity, String> {

    Optional<ProductEntity> findByFactoryAndNameAndDesignAndColourAndPonAndType
            (FactoryEntity factory, String name, String design, String colour, String pon, ProductType type);
}
