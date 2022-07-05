package com.company.service;

import com.company.config.CustomUserDetails;
import com.company.dto.profile.ProfileCreateDTO;
import com.company.dto.profile.ProfileUpdateDTO;
import com.company.dto.profile.ProfileDTO;
import com.company.entity.AttachEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileStatus;
import com.company.exp.BadRequestException;
import com.company.exp.ItemNotFoundException;
import com.company.repository.AttachRepository;
import com.company.repository.ProfileRepository;
import com.company.util.MD5Util;
import com.company.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private AttachRepository attachRepository;
    @Value("${server.url}")
    private String serverUrl;

    // ========================= ADMIN =================================
    public ProfileDTO create(ProfileCreateDTO dto) {
        // name; surname; login; password;
        Optional<ProfileEntity> optional = profileRepository.findByPhoneNumber(dto.getPhoneNumber());
        if (optional.isPresent()) {
            throw new BadRequestException("User already exists");
        }

        ProfileEntity entity = new ProfileEntity();

        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setPassword(MD5Util.getMd5(dto.getPassword()));

        entity.setRole(dto.getRole());
        entity.setStatus(ProfileStatus.ACTIVE);

        profileRepository.save(entity);
        return getProfileDTO(entity);
    }

    public ProfileDTO update(Integer profileId, ProfileCreateDTO dto) {

        Optional<ProfileEntity> optional = profileRepository.findByPhoneNumber(dto.getPhoneNumber());
        if (optional.isPresent() && !optional.get().getId().equals(profileId)) {
            throw new BadRequestException("this phone already exist");
        }

        ProfileEntity entity = get(profileId);
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPassword(MD5Util.getMd5(dto.getPassword()));
        entity.setRole(dto.getRole());

        profileRepository.save(entity);

        return getProfileDTO(entity);
    }

    public List<ProfileDTO> getAllProfileDTO() {

        Iterable<ProfileEntity> iterable = profileRepository.findAll();

        List<ProfileDTO> profileDTOS = new ArrayList<>();
        iterable.forEach(profileEntity -> {

            profileDTOS.add(getProfileDTO(profileEntity));
        });

        return profileDTOS;
    }
    // ========================= GENERAL ===============================

    public ProfileDTO update(ProfileUpdateDTO dto) {

        if (dto.getName().length() < 3 || dto.getName() == null) {
            throw new BadRequestException("name wrong");
        }

        if (dto.getSurname().length() < 3 || dto.getSurname() == null) {
            throw new BadRequestException("surname wrong");
        }

        if (dto.getPassword().length() != 6 || dto.getPassword() == null) {
            throw new BadRequestException("password wrong");
        }

        ProfileEntity entity = getProfile();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPassword(dto.getPassword());

        profileRepository.save(entity);

        return getProfileDTO(entity);
    }

//    private void saveAttach(ProfileEntity entity, ProfileUpdateDTO dto) {
//        if (entity.getPhoto() != null && dto.getAttachId() != null) {
//            //deleted
//            Optional<AttachEntity> optional = attachRepository.findById(entity.getPhoto().getUuid());
//
//            if (optional.isEmpty()) {
//                throw new ItemNotFoundException("Attach not found");
//            }
//
//            AttachEntity attach = optional.get();
//
//            String path = attach.getPath();
//            String uuid = attach.getUuid();
//
//            try {
//                Files.delete(Path.of("attaches/" + path + "/" + uuid));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            Optional<AttachEntity> optional1 = attachRepository.findById(dto.getAttachId());
//
//            if (optional1.isEmpty()) {
//                throw new ItemNotFoundException("Attach not found");
//            }
//
//            entity.setPhoto(optional1.get());
//        }
//
//    }
//    private void saveAttach(ProfileEntity entity, ProfileCreateDTO dto) {
//        if (entity.getPhoto() != null && dto.getAttachId() != null) {
//            //deleted
//            Optional<AttachEntity> optional = attachRepository.findById(entity.getPhoto().getUuid());
//
//            if (optional.isEmpty()) {
//                throw new ItemNotFoundException("Attach not found");
//            }
//
//            AttachEntity attach = optional.get();
//
//            String path = attach.getPath();
//            String uuid = attach.getUuid();
//
//            try {
//                Files.delete(Path.of("attaches/" + path + "/" + uuid));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            Optional<AttachEntity> optional1 = attachRepository.findById(dto.getAttachId());
//
//            if (optional1.isEmpty()) {
//                throw new ItemNotFoundException("Attach not found");
//            }
//
//            entity.setPhoto(optional1.get());
//        }
//
//    }

    public ProfileEntity get(Integer id) {
        return profileRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException("Profile Not found");
        });
    }

    public ProfileDTO getProfileDTO(ProfileEntity entity) {

        ProfileDTO profileDTO = new ProfileDTO();

        profileDTO.setId(entity.getId());
        profileDTO.setName(entity.getName());
        profileDTO.setSurname(entity.getSurname());
        profileDTO.setPhoneNumber(entity.getPhoneNumber());
        profileDTO.setRole(entity.getRole());
        profileDTO.setStatus(entity.getStatus());
        profileDTO.setVisible(entity.getVisible());

        if (entity.getPhoto() != null) {
            profileDTO.setUrl(serverUrl + "attaches/" + entity.getPhoto().getPath() + "/" + entity.getPhoto().getUuid());
        }

        return profileDTO;
    }

    public ProfileDTO changeVisible() {

        ProfileEntity entity = getProfile();
        entity.setVisible(!entity.getVisible());

        profileRepository.save(entity);

        return getProfileDTO(entity);
    }

    public ProfileDTO changeVisibleForAdmin(Integer pId) {

        ProfileEntity entity = get(pId);
        entity.setVisible(!entity.getVisible());

        profileRepository.save(entity);

        return getProfileDTO(entity);
    }

    public ProfileDTO getProfile(Integer id) {

        ProfileEntity profile = get(id);
        return getProfileDTO(profile);
    }

    public ProfileEntity getProfile() {
        CustomUserDetails user = SpringSecurityUtil.getCurrentUser();
//        System.out.println(user.getUsername());
//        return getProfileDTOByEmail(user.getUsername());

        return user.getProfile();
    }

    public void save(ProfileEntity profile) {
        profileRepository.save(profile);
    }

    public ProfileDTO getProfilePublic() {

        ProfileEntity profile = getProfile();
        return getProfileDTO(profile);
    }
}
