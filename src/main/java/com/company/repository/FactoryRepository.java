package com.company.repository;

import com.company.entity.FactoryEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface FactoryRepository extends PagingAndSortingRepository<FactoryEntity, Integer> {

    Optional<FactoryEntity> findByName(String name);
}
