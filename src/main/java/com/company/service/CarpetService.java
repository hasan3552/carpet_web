package com.company.service;

import com.company.dto.product.ProductCreateDTO;
import com.company.dto.product.ProductDTO;
import com.company.dto.product.ProductUpdateDTO;
import com.company.entity.*;
import com.company.enums.ProductStatus;
import com.company.enums.ProductType;
import com.company.exp.ItemNotFoundException;
import com.company.repository.CarpetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarpetService {

    @Autowired
    private CarpetRepository carpetRepository;
    @Autowired
    private FactoryService factoryService;
    @Autowired
    private ProductAttachService productAttachService;
    @Autowired
    @Lazy
    private ProductService productService;

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

    public List<CarpetEntity> paginationForAdmin(int page, int size) {

        Sort sort = Sort.by(Sort.Direction.DESC, "uuid");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CarpetEntity> all = carpetRepository.findAll(pageable);

        return all.getContent();
    }

    public ProductDTO getProductDTO(String uuid) {

        CarpetEntity carpet = get(uuid);
        ProductDTO dto = new ProductDTO();
        dto.setType(ProductType.COUNTABLE);
        dto.setUuid(uuid);
        dto.setName(carpet.getProduct().getName());
        dto.setPon(carpet.getProduct().getPon());
        dto.setAmount(carpet.getAmount());
        dto.setFactory(factoryService.getFactoryDTO(carpet.getProduct().getFactory()));
        dto.setWeight(carpet.getWeight());
        dto.setHeight(carpet.getHeight());
        dto.setDesign(carpet.getProduct().getDesign());
        dto.setCreateDate(carpet.getCreateDate());
        dto.setUrlImageList(productAttachService.getProductAttachUrl(carpet.getProduct()));
        dto.setVisible(carpet.getVisible());
        dto.setStatus(carpet.getStatus());
        dto.setAttachUUID(carpet.getProduct().getUuid());

        return dto;

    }

    public CarpetEntity get(String uuid){

        return carpetRepository.findById(uuid).orElseThrow(() ->{
            throw new ItemNotFoundException("Product not fount");
        });
    }

    public ProductDTO changeVisible(String uuid) {

        CarpetEntity carpet = get(uuid);
        carpet.setVisible(!carpet.getVisible());
        carpetRepository.save(carpet);

        return getProductDTO(uuid);
    }

    public ProductDTO update(String uuid, ProductUpdateDTO dto) {

        CarpetEntity carpet = get(uuid);

        ProductCreateDTO dto1 = new ProductCreateDTO();
        dto1.setColour(dto.getColour());
        dto1.setDesign(dto.getDesign());
        dto1.setName(dto.getName());
        dto1.setFactoryId(dto.getFactoryId());
        dto1.setType(dto.getType());
        dto1.setPon(dto.getPon());
        dto1.setPrice(dto.getPrice());
        dto1.setType(ProductType.COUNTABLE);

        ProductEntity product1 = productService.saveOrGet(dto1);

        carpet.setProduct(product1);
        carpet.setHeight(dto.getHeight());
        carpet.setWeight(dto.getWeight());
        carpet.setAmount(dto.getAmount());

        carpetRepository.save(carpet);

        return getProductDTO(carpet.getUuid());
    }

    public void save(CarpetEntity carpet) {
        carpetRepository.save(carpet);
    }
}
