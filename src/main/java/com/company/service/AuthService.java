package com.company.service;

import com.company.dto.profile.AuthDTO;
import com.company.dto.profile.ProfileDTO;
import com.company.dto.profile.RegistrationDTO;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.exp.BadRequestException;
import com.company.repository.ProfileRepository;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;

    public ProfileDTO login(AuthDTO authDTO) {
        Optional<ProfileEntity> optional = profileRepository
                .findByPhoneNumberAndVisible(authDTO.getPhoneNumber(), Boolean.TRUE);
        if (optional.isEmpty()) {
            throw new BadRequestException("User not found");
        }

        ProfileEntity profile = optional.get();
        if (!profile.getPassword().equals(authDTO.getPassword())) {
            throw new BadRequestException("User not found");
        }

        if (!profile.getStatus().equals(ProfileStatus.ACTIVE)) {
            throw new BadRequestException("No ruxsat");
        }

        ProfileDTO dto = new ProfileDTO();
        dto.setName(profile.getName());
        dto.setSurname(profile.getSurname());
        dto.setPhoneNumber(profile.getPhoneNumber());
        dto.setJwt(JwtUtil.encode(profile.getId(), profile.getRole()));
        dto.setRole(profile.getRole());

        return dto;
    }


    // in progress
    public ProfileDTO registration(RegistrationDTO dto) {

        Optional<ProfileEntity> optional = profileRepository
                .findByPhoneNumberAndVisible(dto.getPhoneNumber(),Boolean.TRUE);
        if (optional.isPresent()) {
            throw new BadRequestException("User already exists");
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setPassword(dto.getPassword());

        entity.setRole(ProfileRole.CUSTOMER);
        profileRepository.save(entity);

        // name; surname; email; password;

        ProfileDTO responseDTO = new ProfileDTO();
        responseDTO.setName(dto.getName());
        responseDTO.setSurname(dto.getSurname());
        responseDTO.setPhoneNumber(dto.getPhoneNumber());
        responseDTO.setJwt(JwtUtil.encode(entity.getId(), entity.getRole()));
        responseDTO.setRole(ProfileRole.CUSTOMER);
        return responseDTO;
    }
}
