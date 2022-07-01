package com.company.service;

import com.company.dto.CarpetCreateDTO;
import com.company.dto.ResponseDTO;
import com.company.dto.factory.FactoryDTO;
import com.company.dto.product.ProductCreateDTO;
import com.company.dto.product.ProductDTO;
import com.company.dto.product.ProductPageDTO;
import com.company.entity.*;
import com.company.enums.AttachStatus;
import com.company.enums.ProductStatus;
import com.company.enums.ProductType;
import com.company.repository.CarpetRepository;
import com.company.repository.DetailRepository;
import com.company.repository.ProductAttachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CarpetService {

    @Autowired
    private CarpetRepository carpetRepository;
    @Autowired
    private ProductAttachRepository productAttachRepository;
    @Autowired
    private ProductAttachService productAttachService;

    public Integer create(ProductEntity product, ProductCreateDTO dto) {

        Optional<CarpetEntity> optional = carpetRepository.
                findByProductAndWeightAndHeight(product, dto.getWeight(), dto.getHeight());

        CarpetEntity carpet;
        if (optional.isPresent()) {
            carpet = optional.get();

            carpet.setStatus(ProductStatus.ACTIVE);
            carpet.setAmount(carpet.getAmount() + dto.getAmount());

        } else {

            CarpetEntity entity = new CarpetEntity();
            entity.setWeight(dto.getWeight());
            entity.setHeight(dto.getHeight());
            entity.setAmount(dto.getAmount());
            entity.setProduct(product);

            carpet = entity;
        }

        carpetRepository.save(carpet);

        return carpet.getAmount();
    }

    public List<CarpetEntity> pagination(int page, int size) {

//        Sort sort = Sort.by(Sort.Direction.DESC, "uuid");
//        Pageable pageable = PageRequest.of(page, size, sort);

//        Page<CarpetEntity> all = carpetRepository.findAll(pageable);

        return carpetRepository.pagination(size,page*size,ProductStatus.ACTIVE.name());
    }

}
