package com.company.repository;

import com.company.entity.ProfileEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProfileRepository extends CrudRepository<ProfileEntity, Integer> {
    Optional<ProfileEntity> findByPhoneNumberAndVisible(String phoneNumber, Boolean visible);

    Optional<ProfileEntity> findByPhoneNumber(String phoneNumber);
}
