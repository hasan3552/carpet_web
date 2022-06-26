package com.company.service;

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
        Optional<ProfileEntity> optional = profileRepository
                .findByPhoneNumberAndVisible(dto.getPhoneNumber(), Boolean.TRUE);
        if (optional.isPresent()) {
            throw new BadRequestException("User already exists");
        }

        ProfileEntity entity = new ProfileEntity();
        if (dto.getAttachId() != null) {

            Optional<AttachEntity> optional1 = attachRepository.findById(dto.getAttachId());
            if (optional1.isEmpty()) {
                throw new ItemNotFoundException("Attach not found");
            }
            entity.setAttach(optional1.get());
        }

        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setPassword(dto.getPassword());

        entity.setRole(dto.getRole());
        entity.setStatus(ProfileStatus.ACTIVE);

        profileRepository.save(entity);
        return getProfileDTO(entity);
    }

    public ProfileDTO update(Integer profileId, ProfileCreateDTO dto) {

        if (dto.getName().length() < 3 || dto.getName() == null) {
            throw new BadRequestException("name wrong");
        }

        if (dto.getSurname().length() < 3 || dto.getSurname() == null) {
            throw new BadRequestException("surname wrong");
        }

        if (dto.getPassword().length() != 6 || dto.getPassword() == null) {
            throw new BadRequestException("password wrong");
        }

        ProfileEntity entity = get(profileId);
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPassword(dto.getPassword());
        entity.setRole(dto.getRole());

        saveAttach(entity, dto);
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

    public ProfileDTO update(Integer profileId, ProfileUpdateDTO dto) {

        if (dto.getName().length() < 3 || dto.getName() == null) {
            throw new BadRequestException("name wrong");
        }

        if (dto.getSurname().length() < 3 || dto.getSurname() == null) {
            throw new BadRequestException("surname wrong");
        }

        if (dto.getPassword().length() != 6 || dto.getPassword() == null) {
            throw new BadRequestException("password wrong");
        }

        ProfileEntity entity = get(profileId);
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPassword(dto.getPassword());

        saveAttach(entity, dto);


        profileRepository.save(entity);

        return getProfileDTO(entity);
    }

    private void saveAttach(ProfileEntity entity, ProfileUpdateDTO dto) {
        if (entity.getAttach() != null && dto.getAttachId() != null) {
            //deleted
            Optional<AttachEntity> optional = attachRepository.findById(entity.getAttach().getUuid());

            if (optional.isEmpty()) {
                throw new ItemNotFoundException("Attach not found");
            }

            AttachEntity attach = optional.get();

            String path = attach.getPath();
            String uuid = attach.getUuid();

            try {
                Files.delete(Path.of("attaches/" + path + "/" + uuid));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Optional<AttachEntity> optional1 = attachRepository.findById(dto.getAttachId());

            if (optional1.isEmpty()) {
                throw new ItemNotFoundException("Attach not found");
            }

            entity.setAttach(optional1.get());
        }

    }
    private void saveAttach(ProfileEntity entity, ProfileCreateDTO dto) {
        if (entity.getAttach() != null && dto.getAttachId() != null) {
            //deleted
            Optional<AttachEntity> optional = attachRepository.findById(entity.getAttach().getUuid());

            if (optional.isEmpty()) {
                throw new ItemNotFoundException("Attach not found");
            }

            AttachEntity attach = optional.get();

            String path = attach.getPath();
            String uuid = attach.getUuid();

            try {
                Files.delete(Path.of("attaches/" + path + "/" + uuid));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Optional<AttachEntity> optional1 = attachRepository.findById(dto.getAttachId());

            if (optional1.isEmpty()) {
                throw new ItemNotFoundException("Attach not found");
            }

            entity.setAttach(optional1.get());
        }

    }

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

        if (entity.getAttach() != null) {
            profileDTO.setUrl(serverUrl + "attaches/" + entity.getAttach().getPath() + "/" + entity.getAttach().getUuid());
        }

        return profileDTO;
    }

    public ProfileDTO changeVisible(Integer profileId) {

        ProfileEntity entity = get(profileId);
        entity.setVisible(!entity.getVisible());

        profileRepository.save(entity);

        return getProfileDTO(entity);
    }

    public ProfileDTO getProfile(Integer id) {

        ProfileEntity profile = get(id);
        return getProfileDTO(profile);
    }

}
