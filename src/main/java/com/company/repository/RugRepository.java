package com.company.repository;

import com.company.entity.CarpetEntity;
import com.company.entity.FactoryEntity;
import com.company.entity.ProductEntity;
import com.company.entity.RugEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RugRepository extends PagingAndSortingRepository<RugEntity, String> {

    Optional<RugEntity> findByProductAndWeight(ProductEntity product, Double weight);


    @Query(value = "SELECT * " +
            " FROM rug " +
            " Where  status =:status " +
            " and visible " +
            " order by create_date " +
            " offset :offset " +
            " limit :limit",
            nativeQuery = true)
    List<RugEntity> pagination(@Param("limit") Integer limit,
                                  @Param("offset") Integer offset,
                                  @Param("status") String status);
}
