package com.company.repository;

import com.company.entity.CarpetEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CarpetRepository extends PagingAndSortingRepository<CarpetEntity, String> {
}
