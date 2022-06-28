package com.company.repository;

import com.company.entity.FactoryEntity;
import com.company.enums.FactoryStatus;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface FactoryRepository extends PagingAndSortingRepository<FactoryEntity, Integer> {

    Optional<FactoryEntity> findByName(String name);

    List<FactoryEntity> findAllByStatusAndVisible(FactoryStatus status, Boolean visible);

    Optional<FactoryEntity> findByKey(String key);
}
