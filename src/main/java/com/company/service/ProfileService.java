package com.company.service;

import com.company.dto.profile.ProfileDTO;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.exp.BadRequestException;
import com.company.exp.ItemNotFoundException;
import com.company.repository.ProfileRepository;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    // admin
    public ProfileDTO create(ProfileDTO dto) {
        // name; surname; login; password;
        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
        if (optional.isPresent()) {
            throw new BadRequestException("User already exists");
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());

        entity.setRole(ProfileRole.USER);
        entity.setStatus(ProfileStatus.ACTIVE);

        profileRepository.save(entity);
        dto.setId(entity.getId());
        dto.setJwt(JwtUtil.encode(entity.getId(),entity.getRole()));
        return dto;
    }

    public void update(Integer profileId, ProfileDTO dto) {
        ProfileEntity entity = get(profileId);
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        profileRepository.save(entity);

    }

    public ProfileEntity get(Integer id) {
        return profileRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException("Profile Not found");
        });
    }

    public ProfileDTO getProfileDTOById(Integer id) {

        Optional<ProfileEntity> optional = profileRepository.findById(id);
        if (optional.isEmpty()){
            throw new ItemNotFoundException("This user not fount");
        }

        ProfileEntity profileEntity = optional.get();
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setEmail(profileEntity.getEmail());
        profileDTO.setCreatedDate(profileEntity.getCreatedDate());
        profileDTO.setSurname(profileEntity.getSurname());
        profileDTO.setName(profileEntity.getName());
        profileDTO.setId(profileEntity.getId());

        return profileDTO;
    }

    public List<ProfileDTO> getAllProfileDTOById() {

        Iterable<ProfileEntity> iterable = profileRepository.findAll();

        List<ProfileDTO> profileDTOS = new ArrayList<>();
        iterable.forEach(profileEntity -> {

            ProfileDTO profileDTO = new ProfileDTO();
            profileDTO.setEmail(profileEntity.getEmail());
            profileDTO.setCreatedDate(profileEntity.getCreatedDate());
            profileDTO.setSurname(profileEntity.getSurname());
            profileDTO.setName(profileEntity.getName());
            profileDTO.setId(profileEntity.getId());

            profileDTOS.add(profileDTO);
        });

        return profileDTOS;
    }

    public ProfileDTO changeVisible(Integer profileId) {

        ProfileEntity entity = get(profileId);
        entity.setVisible(!entity.getVisible());

        profileRepository.save(entity);

        return getProfileDTOById(entity.getId());
    }
}
