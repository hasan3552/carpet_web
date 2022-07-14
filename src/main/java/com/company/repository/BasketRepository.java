package com.company.repository;

import com.company.entity.BasketEntity;
import com.company.enums.BasketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BasketRepository extends JpaRepository<BasketEntity, Integer> {


    List<BasketEntity> findAllByStatusAndVisible(BasketStatus status, Boolean visible);

    Page<BasketEntity> findAllByVisibleAndStatus(Pageable pageable, Boolean aTrue, BasketStatus status);

// id createdDate status product.(id name attachUrl ) factory(id name attachUrl)
//    @Query("select b.* " +
//            "from basket as b " +
//            "where b.status = :status " +
//            "and visible ")
//    List<BasketShortInfo> pagination(@Param("status") BasketStatus status);
}
