package com.company.service;

import com.company.dto.product.ProductCreateDTO;
import com.company.dto.product.ProductDTO;
import com.company.entity.*;
import com.company.enums.ProductStatus;
import com.company.enums.ProductType;
import com.company.exp.ItemNotFoundException;
import com.company.repository.DetailRepository;
import com.company.repository.RugRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RugService {

    @Autowired
    private RugRepository rugRepository;
    @Autowired
    private FactoryService factoryService;
    @Autowired
    private ProductAttachService productAttachService;
    public void create(ProductEntity product, ProductCreateDTO dto) {

        for (int i = 0; i < dto.getAmount(); i++) {

            RugEntity rug = new RugEntity();
            rug.setHeight(dto.getHeight());
            rug.setWeight(dto.getWeight());
            rug.setProduct(product);

            rugRepository.save(rug);

        }
    }

    public List<RugEntity> pagination(int page, int size) {
//        Sort sort = Sort.by(Sort.Direction.DESC, "uuid");
//        Pageable pageable = PageRequest.of(page, size, sort);
//
//        Page<RugEntity> all = rugRepository.findAll(pageable);
//
//        List<RugEntity> list = all.getContent();
//
//        return list;
        return rugRepository.pagination(size,page*size,ProductStatus.ACTIVE.name());
    }

    public List<RugEntity> paginationForAdmin(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "uuid");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<RugEntity> all = rugRepository.findAll(pageable);

        return all.getContent();

    }


    public ProductDTO getProductDTO(String uuid) {

        RugEntity rug = get(uuid);
        ProductDTO dto = new ProductDTO();
        dto.setType(ProductType.COUNTABLE);
        dto.setUuid(uuid);
        dto.setName(rug.getProduct().getName());
        dto.setPon(rug.getProduct().getPon());
       // dto.setAmount(rug.getAmount());
        dto.setFactory(factoryService.getFactoryDTO(rug.getProduct().getFactory()));
        dto.setWeight(rug.getWeight());
        dto.setHeight(rug.getHeight());
        dto.setDesign(rug.getProduct().getDesign());
        dto.setCreateDate(rug.getCreateDate());
        dto.setUrlImageList(productAttachService.getProductAttachUrl(rug.getProduct()));

        return dto;

    }

    private RugEntity get(String uuid) {

        return rugRepository.findById(uuid).orElseThrow(() ->{
            throw new ItemNotFoundException("Rug not fount");
        });
    }
}
