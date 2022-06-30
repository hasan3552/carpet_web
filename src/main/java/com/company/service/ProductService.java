package com.company.service;

import com.company.dto.factory.FactoryDTO;
import com.company.dto.product.ProductCreateDTO;
import com.company.dto.product.ProductDTO;
import com.company.entity.*;
import com.company.enums.AttachStatus;
import com.company.enums.ProductType;
import com.company.exp.BadRequestException;
import com.company.exp.ItemNotFoundException;
import com.company.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CarpetService carpetService;
    @Autowired
    private RugService rugService;
    @Autowired
    private DetailService detailService;
    @Autowired
    private ProductAttachRepository productAttachRepository;
    @Autowired
    private FactoryService factoryService;

    @Value("${attach.folder}")
    private String attachFolder;

    @Value("${server.url}")
    private String serverUrl;


    public ProductDTO create(Integer profileId, ProductCreateDTO dto) {

        ProductEntity product;
        Optional<ProductEntity> optional = productRepository.findByFactoryAndNameAndDesignAndColourAndPonAndType
                (new FactoryEntity(dto.getFactoryId()), dto.getName(), dto.getDesign(), dto.getColour(), dto.getPon(), dto.getType());

        product = optional.orElseGet(() -> saveNewProduct(dto));

        if (dto.getType().equals(ProductType.COUNTABLE)) {

           Integer amount = carpetService.create(product, dto);
           dto.setAmount(amount);

        } else if (dto.getType().equals(ProductType.UNCOUNTABLE)) {

            rugService.create(product, dto);

        } else {
            throw new BadRequestException("Product type wrong");
        }

        detailService.saveDetail(profileId, dto);

        return getProductDTO(product,dto);
    }

    public ProductEntity get(String productId){

        Optional<ProductEntity> optional1 = productRepository.findById(productId);
        if (optional1.isEmpty()){
            throw new ItemNotFoundException("Product not found");
        }

        return optional1.get();
    }

    private ProductEntity saveNewProduct(ProductCreateDTO dto) {

        ProductEntity entity = new ProductEntity();
        entity.setColour(dto.getColour());
        entity.setDesign(dto.getDesign());
        entity.setFactory(factoryService.get(dto.getFactoryId()));
        entity.setPon(dto.getPon());
        entity.setName(dto.getName());
        entity.setType(dto.getType());

        productRepository.save(entity);
        return entity;
    }

    private ProductDTO getProductDTO(ProductEntity product, ProductCreateDTO dto) {

        List<ProductAttachEntity> list = productAttachRepository
                .findAllByProductAndStatusAndVisible(product, AttachStatus.ACTIVE, Boolean.TRUE);

        List<String> urlList = new ArrayList<>();
        list.forEach(productAttachEntity -> {

            urlList.add((serverUrl + "attach/open?fileId=" + productAttachEntity.getAttach().getUuid()));

        });

        return new ProductDTO(product.getUuid(), factoryService.getFactoryDTO(product.getFactory()),
                dto.getName(), dto.getDesign(), dto.getColour(), dto.getHeight(), dto.getWeight(),
                dto.getPon(), product.getCreateDate(), dto.getAmount(), dto.getType(), urlList);
    }

    public PageImpl pagination(int page, int size, ProductType type) {
        // page = 1
//       Iterable<TypesEntity> all = typesRepository.pagination(size, size * (page - 1));
//        long totalAmount = typesRepository.countAllBy();
//        long totalAmount = all.getTotalElements();
//        int totalPages = all.getTotalPages();

//        TypesPaginationDTO paginationDTO = new TypesPaginationDTO(totalAmount, dtoList);
//        return paginationDTO;

//        if (type.equals(ProductType.COUNTABLE)){
//
//            List<CarpetEntity> list = carpetService.pagination(page, size);
//
//
//        } else if (type.equals(ProductType.UNCOUNTABLE)) {
//            rugService.pagination(page, size)
//        }else {
//            throw new BadRequestException("Wrong request");
//        }
       return null;
    }
}
