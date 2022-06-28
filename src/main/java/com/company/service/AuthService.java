package com.company.service;

import com.company.dto.profile.ProfileLoginResponseDTO;
import com.company.dto.profile.AuthDTO;
import com.company.dto.profile.ProfileDTO;
import com.company.dto.profile.RegistrationDTO;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.exp.BadRequestException;
import com.company.exp.NoPermissionException;
import com.company.repository.ProfileRepository;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Value("${server.url}")
    private String serverUrl;

    public ProfileLoginResponseDTO login(AuthDTO authDTO) {
        Optional<ProfileEntity> optional = profileRepository
                .findByPhoneNumber(authDTO.getPhoneNumber());
        if (optional.isEmpty()) {
            throw new BadRequestException("User not found");
        }

        ProfileEntity profile = optional.get();
        if (!profile.getPassword().equals(authDTO.getPassword())) {
            throw new BadRequestException("User not found");
        }

        if (!profile.getStatus().equals(ProfileStatus.ACTIVE)) {
            throw new NoPermissionException("No access");
        }

        if (!profile.getVisible()) {
            profile.setVisible(Boolean.TRUE);
            profileRepository.save(profile);
        }

        ProfileLoginResponseDTO dto = new ProfileLoginResponseDTO();
        dto.setName(profile.getName());
        dto.setSurname(profile.getSurname());
        dto.setPhoneNumber(profile.getPhoneNumber());
        dto.setJwt(JwtUtil.encode(profile.getId(), profile.getRole()));
        dto.setRole(profile.getRole());
        if (profile.getPhoto() != null){
            dto.setUrl(serverUrl+"attach/open?fileId="+profile.getPhoto().getUuid());
        }

        return dto;
    }


    // in progress
    public ProfileDTO registration(RegistrationDTO dto) {

        Optional<ProfileEntity> optional = profileRepository.findByPhoneNumber(dto.getPhoneNumber());

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

        // name; surname; phone; password;

        ProfileDTO responseDTO = new ProfileDTO();
        responseDTO.setName(dto.getName());
        responseDTO.setSurname(dto.getSurname());
        responseDTO.setPhoneNumber(dto.getPhoneNumber());
        responseDTO.setJwt(JwtUtil.encode(entity.getId(), entity.getRole()));
        responseDTO.setRole(ProfileRole.CUSTOMER);
        return responseDTO;
    }
}
