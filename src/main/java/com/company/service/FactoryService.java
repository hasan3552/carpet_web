package com.company.service;

import com.company.dto.factory.FactoryDTO;
import com.company.entity.FactoryEntity;
import com.company.enums.ProfileRole;
import com.company.exp.BadRequestException;
import com.company.exp.ItemNotFoundException;
import com.company.exp.NoPermissionException;
import com.company.exp.NullFieldException;
import com.company.repository.FactoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FactoryService {

    @Autowired
    private FactoryRepository factoryRepository;
    @Autowired
    private ProfileService profileService;

    public FactoryDTO created(String name, Integer profileId) {

        isValid(name, profileId);

        FactoryEntity entity = new FactoryEntity();
        entity.setName(name);

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

        return factoryDTO;
    }

    public FactoryDTO update(String name, Integer profileId, Integer factoryId) {

        isValid(name, profileId);

        FactoryEntity entity = get(factoryId);

        entity.setName(name);
        entity.setKey("factory_" + name);

        factoryRepository.save(entity);

        return getFactoryDTO(entity);
    }

    private void isValid(String name, Integer profileId) {

        if (profileService.get(profileId).getRole().equals(ProfileRole.CUSTOMER)) {
            throw new NoPermissionException("Not Access");
        }

        if (name == null || name.isEmpty()) {
            throw new NullFieldException("factory name wrong");
        }

        Optional<FactoryEntity> optional = factoryRepository.findByName(name);
        if (optional.isPresent()) {
            throw new BadRequestException("This factory already exist");
        }
    }

    public FactoryEntity get(Integer factoryId) {
        return factoryRepository.findById(factoryId).orElseThrow(() -> {
            throw new ItemNotFoundException("Factory Not found");
        });
    }

    public FactoryDTO getFactory(Integer factoryId) {

        FactoryEntity entity = get(factoryId);

        return getFactoryDTO(entity);
    }

    public List<FactoryDTO> getFactoryList() {

        Iterable<FactoryEntity> all = factoryRepository.findAll();

        List<FactoryDTO> list = new ArrayList<>();
        all.forEach(factoryEntity -> {
            list.add(getFactoryDTO(factoryEntity));
        });

        return list;
    }

    public FactoryDTO changeVisible(Integer factoryId) {

        FactoryEntity entity = get(factoryId);
        entity.setVisible(!entity.getVisible());

        return getFactoryDTO(entity);
    }
}
