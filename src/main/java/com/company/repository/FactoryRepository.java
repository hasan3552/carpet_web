package com.company.repository;

import com.company.entity.FactoryEntity;
import com.company.enums.FactoryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FactoryRepository extends PagingAndSortingRepository<FactoryEntity, Integer> {

    Optional<FactoryEntity> findByName(String name);

    List<FactoryEntity> findAllByStatusAndVisible(FactoryStatus status, Boolean visible);

    Optional<FactoryEntity> findByKey(String key);


    @Query(value = "select * " +
            "from factory" +
            " where visible " +
            "and status = 'ACTIVE' " +
            "order by id " +
            "limit:limit  " +
            "offset:offset", nativeQuery = true)
    List<FactoryEntity> pagination(@Param("limit") Integer limit,@Param("offset") Integer offset);
}
