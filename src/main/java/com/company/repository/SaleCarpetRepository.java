package com.company.repository;

import com.company.entity.SaleCarpetEntity;
import com.company.entity.SaleRugEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleCarpetRepository extends JpaRepository<SaleCarpetEntity, Integer> {

    List<SaleCarpetEntity> findAllByCreatedDateBetween(LocalDateTime from, LocalDateTime to);
}
