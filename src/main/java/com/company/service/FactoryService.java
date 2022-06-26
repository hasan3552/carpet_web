package com.company.service;

import com.company.dto.factory.FactoryCreateDTO;
import com.company.dto.factory.FactoryDTO;
import com.company.entity.AttachEntity;
import com.company.entity.FactoryEntity;
import com.company.enums.FactoryStatus;
import com.company.enums.ProfileRole;
import com.company.exp.BadRequestException;
import com.company.exp.ItemNotFoundException;
import com.company.exp.NoPermissionException;
import com.company.exp.NullFieldException;
import com.company.repository.AttachRepository;
import com.company.repository.FactoryRepository;
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
public class FactoryService {

    @Autowired
    private FactoryRepository factoryRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private AttachRepository attachRepository;
    @Value("${server.url}")
    private String serverUrl;

    public FactoryDTO created(FactoryCreateDTO dto, Integer profileId) {

        isValid(dto, profileId);

        FactoryEntity entity = new FactoryEntity();
        entity.setName(dto.getName());

        if (dto.getAttachId() != null){
            entity.setAttach(new AttachEntity(dto.getAttachId()));
        }

        factoryRepository.save(entity);

        return getFactoryDTO(entity);

    }


    public FactoryDTO getFactoryDTO(FactoryEntity entity) {

        FactoryDTO factoryDTO = new FactoryDTO();

        factoryDTO.setCreatedDate(entity.getCreatedDate());
        factoryDTO.setId(entity.getId());
        factoryDTO.setKey(entity.getKey());
        factoryDTO.setStatus(entity.getStatus());
        factoryDTO.setVisible(entity.getVisible());
        factoryDTO.setName(entity.getName());
        factoryDTO.setPhotoUrl(serverUrl+"/attach/open/"+entity.getAttach().getUuid());

        return factoryDTO;
    }

    public FactoryDTO update(FactoryCreateDTO dto, Integer profileId, Integer factoryId) {

        isValid(dto, profileId);

        FactoryEntity entity = get(factoryId);

        entity.setName(dto.getName());
        entity.setKey("factory_" + dto.getName());

        if (entity.getAttach() != null && dto.getAttachId() != null){
            //deleted
            Optional<AttachEntity> optional = attachRepository.findById(entity.getAttach().getUuid());

            if (optional.isEmpty()){
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

        }
        if (dto.getAttachId() != null){
            entity.setAttach(new AttachEntity(dto.getAttachId()));
        }

        factoryRepository.save(entity);

        return getFactoryDTO(entity);
    }

    private void isValid(FactoryCreateDTO dto, Integer profileId) {

        if (profileService.get(profileId).getRole().equals(ProfileRole.CUSTOMER)) {
            throw new NoPermissionException("Not Access");
        }

        if (dto.getName() == null || dto.getName().isEmpty()) {
            throw new NullFieldException("factory name wrong");
        }

        Optional<FactoryEntity> optional = factoryRepository.findByName(dto.getName());
        if (optional.isPresent()) {
            throw new BadRequestException("This factory already exist");
        }

        if (dto.getAttachId() != null ){
            Optional<AttachEntity> optional1 = attachRepository.findById(dto.getAttachId());

            if (optional1.isEmpty()){
                throw new ItemNotFoundException("Attach not fount");
            }
        }


    }

    public FactoryEntity get(Integer factoryId) {
        return factoryRepository.findById(factoryId).orElseThrow(() -> {
            throw new ItemNotFoundException("Factory Not found");
        });
    }

    public FactoryDTO getFactory(Integer factoryId) {

        FactoryEntity entity = get(factoryId);

        if (!entity.getVisible()){
            throw new ItemNotFoundException("Factory not found");
        }

        if (entity.getStatus().equals(FactoryStatus.BLOCKED)){
            throw new ItemNotFoundException("Factory not found");
        }

        return getFactoryDTO(entity);
    }

    public List<FactoryDTO> getFactoryList(Integer profileId) {

            if (profileService.get(profileId).getRole().equals(ProfileRole.CUSTOMER)){
                throw new NoPermissionException("No access");
            }

            Iterable<FactoryEntity> all = factoryRepository.findAll();

            List<FactoryDTO> list = new ArrayList<>();
            all.forEach(factoryEntity -> {
                list.add(getFactoryDTO(factoryEntity));
            });

            return list;
    }

    public FactoryDTO changeVisible(Integer factoryId, Integer profileId) {

        if (profileService.get(profileId).getRole().equals(ProfileRole.CUSTOMER)){
            throw new NoPermissionException("No access");
        }

        FactoryEntity entity = get(factoryId);
        entity.setVisible(!entity.getVisible());

        return getFactoryDTO(entity);
    }

    public List<FactoryDTO> getAllListByStatusAndVisible() {

        List<FactoryEntity> list = factoryRepository
                .findAllByStatusAndVisible(FactoryStatus.ACTIVE, Boolean.TRUE);

        List<FactoryDTO> dtos = new ArrayList<>();
        list.forEach(factoryEntity -> {
            dtos.add(getFactoryDTO(factoryEntity));
        });

        return dtos;
    }
}
