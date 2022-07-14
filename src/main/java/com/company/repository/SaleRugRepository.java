package com.company.repository;

import com.company.entity.RugEntity;
import com.company.entity.SaleRugEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleRugRepository extends JpaRepository<SaleRugEntity, Integer> {

    List<SaleRugEntity> findAllByCreatedDateBetween(LocalDateTime from, LocalDateTime to);

}
