package com.company.repository;

import com.company.entity.DetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailRepository extends JpaRepository<DetailEntity, Integer> {
}
