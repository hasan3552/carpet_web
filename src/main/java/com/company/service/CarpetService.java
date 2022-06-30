package com.company.service;

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
        if (optional.isPresent()){
            carpet = optional.get();

            carpet.setStatus(ProductStatus.ACTIVE);
            carpet.setAmount(carpet.getAmount()+dto.getAmount());

        }else {

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

        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CarpetEntity> all = carpetRepository.findAll(pageable);

        List<CarpetEntity> list = all.getContent();

        return list;
       }



//
//
//    private ProductDTO newCarpetSaveDB(ProductCreateDTO dto) {
//
//        CarpetEntity carpet = new CarpetEntity();
//        carpet.setFactory(new FactoryEntity(dto.getFactoryId()));
//        carpet.setName(dto.getName().toUpperCase());
//        carpet.setDesign(dto.getDesign().toUpperCase());
//        carpet.setColour(dto.getColour().toUpperCase());
//        carpet.setAmount(dto.getAmount());
//        carpet.setHeight(dto.getHeight());
//        carpet.setWeight(dto.getWeight());
//        carpet.setPon(dto.getPon().toUpperCase());
//
//        carpetRepository.save(carpet);
//
//        return getProductDTO(carpet);
//    }
//
//    private ProductDTO getProductDTO(CarpetEntity carpet) {
//
//        ProductDTO productDTO = new ProductDTO();
//        productDTO.setAmount(carpet.getAmount());
//        productDTO.setColour(carpet.getColour());
//        productDTO.setDesign(carpet.getDesign());
//        productDTO.setFactory(factoryService.getFactoryDTO(carpet.getFactory()));
//        productDTO.setHeight(carpet.getHeight());
//        productDTO.setWeight(carpet.getWeight());
//        productDTO.setName(carpet.getName());
//        productDTO.setPon(carpet.getPon());
//        productDTO.setStatus(carpet.getStatus());
//        productDTO.setCreateDate(carpet.getCreateDate());
//        productDTO.setUuid(carpet.getUuid());
//        productDTO.setType(ProductType.COUNTABLE);
//
//        productDTO.setUrlImageList(productAttachService.getProductImage(carpet));
//
//        return productDTO;
//    }
}
