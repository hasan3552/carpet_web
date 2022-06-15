package com.company.repository;

import com.company.entity.RugEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RugRepository extends PagingAndSortingRepository<RugEntity, String> {

}
