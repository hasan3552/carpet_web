package com.company.repository;

import com.company.entity.CarpetEntity;
import com.company.entity.FactoryEntity;
import com.company.entity.ProductEntity;
import com.company.enums.ProductStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CarpetRepository extends PagingAndSortingRepository<CarpetEntity, String> {

    Optional<CarpetEntity> findByProductAndWeightAndHeight(ProductEntity product, Double weight, Double height);


    @Query(value = "SELECT * " +
            " FROM carpet " +
            " Where  status =:status " +
            " and visible " +
            " order by create_date " +
            " offset :offset " +
            " limit :limit",
            nativeQuery = true)
    List<CarpetEntity> pagination(@Param("limit") Integer limit,
                                  @Param("offset") Integer offset,
                                  @Param("status") String status);
}
