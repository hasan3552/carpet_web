package com.company.repository;

import com.company.entity.RugEntity;
import com.company.entity.SaleRugEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRugRepository extends JpaRepository<SaleRugEntity, Integer> {


}
