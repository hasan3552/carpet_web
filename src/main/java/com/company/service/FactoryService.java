package com.company.service;

import com.company.dto.factory.FactoryCreateDTO;
import com.company.dto.factory.FactoryDTO;
import com.company.dto.factory.FactoryUpdateDTO;
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
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
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

    public FactoryDTO created(FactoryCreateDTO dto) {

        FactoryEntity entity = new FactoryEntity();
        entity.setName(dto.getName());
        entity.setKey("factory_" + dto.getName());

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
        if (entity.getAttach() != null) {
            factoryDTO.setPhotoUrl(serverUrl + "attach/open?fileId=" + entity.getAttach().getUuid());
        }

        return factoryDTO;
    }

    public FactoryDTO update(FactoryUpdateDTO dto, Integer factoryId) {

        FactoryEntity entity = get(factoryId);
        Optional<FactoryEntity> optional = factoryRepository.findByName(dto.getName());

        if (optional.isPresent() && !optional.get().getId().equals(dto.getId())) {
            throw new BadRequestException("This factory already exist");
        }

        entity.setName(dto.getName());
        entity.setKey("factory_" + dto.getName());
        entity.setStatus(dto.getStatus());
        entity.setVisible(dto.getVisible());

        factoryRepository.save(entity);

        return getFactoryDTO(entity);
    }

    public FactoryEntity get(Integer factoryId) {
        return factoryRepository.findById(factoryId).orElseThrow(() -> {
            throw new ItemNotFoundException("Factory Not found");
        });
    }

    public FactoryDTO getFactory(Integer factoryId) {

        FactoryEntity entity = get(factoryId);

        if (!entity.getVisible()) {
            throw new ItemNotFoundException("Factory not found");
        }

        if (entity.getStatus().equals(FactoryStatus.BLOCKED)) {
            throw new ItemNotFoundException("Factory not found");
        }

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

    public List<FactoryDTO> getAllListByStatusAndVisible() {

        List<FactoryEntity> list = factoryRepository
                .findAllByStatusAndVisible(FactoryStatus.ACTIVE, Boolean.TRUE);

        List<FactoryDTO> dtos = new ArrayList<>();
        list.forEach(factoryEntity -> {
            dtos.add(getFactoryDTO(factoryEntity));
        });

        return dtos;
    }

    public PageImpl pagination(int page, int size) {
        // page = 1
//       Iterable<TypesEntity> all = typesRepository.pagination(size, size * (page - 1));
//        long totalAmount = typesRepository.countAllBy();
//        long totalAmount = all.getTotalElements();
//        int totalPages = all.getTotalPages();

//        TypesPaginationDTO paginationDTO = new TypesPaginationDTO(totalAmount, dtoList);
//        return paginationDTO;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<FactoryEntity> all = factoryRepository.findAll(pageable);

        List<FactoryEntity> list = all.getContent();

        List<FactoryDTO> dtoList = new LinkedList<>();

        list.forEach(factory -> dtoList.add(getFactoryDTO(factory)));

        return new PageImpl(dtoList, pageable, all.getTotalElements());
    }

    public List<FactoryDTO> paginationForAdmin(Integer page, Integer size) {

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        //Page<FactoryEntity> all = factoryRepository.findByPagination(pageable, Boolean.TRUE, FactoryStatus.ACTIVE);

        List<FactoryEntity> list = factoryRepository.pagination(size,page*size);

        List<FactoryDTO> dtoList = new LinkedList<>();

        list.forEach(factory -> dtoList.add(getFactoryDTO(factory)));

        return dtoList;
    }
}
