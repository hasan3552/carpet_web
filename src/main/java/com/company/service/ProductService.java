package com.company.service;

import com.company.dto.product.ProductCreateDTO;
import com.company.dto.product.ProductDTO;
import com.company.dto.product.ProductPageDTO;
import com.company.dto.product.ProductUpdateDTO;
import com.company.entity.*;
import com.company.enums.ProductType;
import com.company.exp.BadRequestException;
import com.company.exp.ItemNotFoundException;
import com.company.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.company.util.CurrencyUtil.calcPrice;

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
    private ProductAttachService productAttachService;
    @Autowired
    private FactoryService factoryService;

    @Value("${attach.folder}")
    private String attachFolder;

    @Value("${server.url}")
    private String serverUrl;

    public ProductDTO create(Integer profileId, ProductCreateDTO dto) {

        ProductEntity product = saveOrGet(dto);

        if (dto.getType().equals(ProductType.COUNTABLE)) {

            Integer amount = carpetService.create(product, dto);
            dto.setAmount(amount);

        } else if (dto.getType().equals(ProductType.UNCOUNTABLE)) {

            rugService.create(product, dto);

        } else {
            throw new BadRequestException("Product type wrong");
        }

        detailService.saveDetail(profileId, dto);

        return getProductDTO(product, dto);
    }

    public ProductEntity get(String productId) {

        Optional<ProductEntity> optional1 = productRepository.findById(productId);
        if (optional1.isEmpty()) {
            throw new ItemNotFoundException("Product not found");
        }

        return optional1.get();
    }

    public ProductEntity saveOrGet(ProductCreateDTO dto){
        Optional<ProductEntity> optional = productRepository.findByFactoryAndNameAndDesignAndColourAndPonAndType
                (new FactoryEntity(dto.getFactoryId()), dto.getName().toUpperCase(), dto.getDesign().toUpperCase(),
                        dto.getColour().toUpperCase(), dto.getPon().toUpperCase(), dto.getType());

        ProductEntity product;
        product = optional.orElseGet(() -> saveNewProduct(dto));
        product.setPrice(dto.getPrice());

        productRepository.save(product);

        return product;
    }


    private ProductEntity saveNewProduct(ProductCreateDTO dto) {

        ProductEntity entity = new ProductEntity();
        entity.setColour(dto.getColour().toUpperCase());
        entity.setDesign(dto.getDesign().toUpperCase());
        entity.setFactory(factoryService.get(dto.getFactoryId()));
        entity.setPon(dto.getPon().toUpperCase());
        entity.setName(dto.getName().toUpperCase());
        entity.setType(dto.getType());
        entity.setPrice(dto.getPrice());

        return entity;
    }

    private ProductDTO getProductDTO(ProductEntity product, ProductCreateDTO dto) {

        List<String> urlList = productAttachService.getProductAttachUrl(product);

        return new ProductDTO(product.getUuid(), factoryService.getFactoryDTO(product.getFactory()),
                dto.getName(), dto.getDesign(), dto.getColour(), dto.getHeight(), dto.getWeight(),
                dto.getPon(), product.getCreateDate(), dto.getAmount(), dto.getType(), product.getPrice(), urlList);
    }

    public List<ProductPageDTO> pagination(int page, int size, ProductType type) {
        // page = 1
//       Iterable<TypesEntity> all = typesRepository.pagination(size, size * (page - 1));
//        long totalAmount = typesRepository.countAllBy();
//        long totalAmount = all.getTotalElements();
//        int totalPages = all.getTotalPages();

//        TypesPaginationDTO paginationDTO = new TypesPaginationDTO(totalAmount, dtoList);
//        return paginationDTO;

        if (type.equals(ProductType.COUNTABLE)) {

            List<CarpetEntity> list = carpetService.pagination(page, size);
            return getPageDTOListCarpet(list);

        } else if (type.equals(ProductType.UNCOUNTABLE)) {

            List<RugEntity> list = rugService.pagination(page, size);
            return getPageDTOList(list);

        } else {
            throw new BadRequestException("Wrong request");
        }
    }

    public List<ProductPageDTO> paginationForAdmin(int page, int size, ProductType type) {
        // page = 1
//       Iterable<TypesEntity> all = typesRepository.pagination(size, size * (page - 1));
//        long totalAmount = typesRepository.countAllBy();
//        long totalAmount = all.getTotalElements();
//        int totalPages = all.getTotalPages();

//        TypesPaginationDTO paginationDTO = new TypesPaginationDTO(totalAmount, dtoList);
//        return paginationDTO;

        if (type.equals(ProductType.COUNTABLE)) {

            List<CarpetEntity> list = carpetService.paginationForAdmin(page, size);
            return getPageDTOListCarpet(list);

        } else if (type.equals(ProductType.UNCOUNTABLE)) {

            List<RugEntity> list = rugService.paginationForAdmin(page, size);
            return getPageDTOList(list);

        } else {
            throw new BadRequestException("Wrong request");
        }
    }

    private List<ProductPageDTO> getPageDTOListCarpet(List<CarpetEntity> list) {

        List<ProductPageDTO> pageDTOS = new ArrayList<>();

        list.forEach(carpet -> pageDTOS.add(getPageDTO(carpet)));

        return pageDTOS;
    }

    private List<ProductPageDTO> getPageDTOList(List<RugEntity> list) {

        List<ProductPageDTO> pageDTOS = new ArrayList<>();

        list.forEach(rug -> pageDTOS.add(getPageDTO(rug)));

        return pageDTOS;
    }

    private ProductPageDTO getPageDTO(CarpetEntity carpet) {

        ProductPageDTO productPageDTO = new ProductPageDTO();
        productPageDTO.setCreatedDate(carpet.getCreateDate());
        productPageDTO.setWeight(carpet.getWeight());
        productPageDTO.setHeight(carpet.getHeight());
        productPageDTO.setFactoryName(carpet.getProduct().getFactory().getName());
        productPageDTO.setName(carpet.getProduct().getName());
        productPageDTO.setUuid(carpet.getUuid());
        productPageDTO.setPrice(calcPrice(carpet.getHeight(), carpet.getWeight(), carpet.getProduct().getPrice()));

        productPageDTO.setImageUrlList(productAttachService.getProductAttachUrl(carpet.getProduct()));

        return productPageDTO;
    }



    private ProductPageDTO getPageDTO(RugEntity rug) {

        ProductPageDTO productPageDTO = new ProductPageDTO();
        productPageDTO.setCreatedDate(rug.getCreateDate());
        productPageDTO.setWeight(rug.getWeight());
        productPageDTO.setHeight(rug.getHeight());
        productPageDTO.setFactoryName(rug.getProduct().getFactory().getName());
        productPageDTO.setName(rug.getProduct().getName());
        productPageDTO.setUuid(rug.getUuid());
        productPageDTO.setPrice(calcPrice(1.0, rug.getWeight(), rug.getProduct().getPrice()));

        productPageDTO.setImageUrlList(productAttachService.getProductAttachUrl(rug.getProduct()));

        return productPageDTO;
    }

    public ProductDTO getProduct(String uuid, ProductType type) {

        if (type.equals(ProductType.COUNTABLE)) {

            return carpetService.getProductDTO(uuid);
        } else {

            return rugService.getProductDTO(uuid);
        }
    }

    public ProductDTO getProductForPublic(String uuid, ProductType type) {

        if (type.equals(ProductType.COUNTABLE)) {

            ProductDTO productDTO = carpetService.getProductDTO(uuid);
            productDTO.setPrice(calcPrice(productDTO.getHeight(), productDTO.getWeight(), productDTO.getPrice()));
            return productDTO;

        }

        ProductDTO productDTO = rugService.getProductDTO(uuid);
        productDTO.setPrice(calcPrice(productDTO.getHeight(), productDTO.getWeight(), productDTO.getPrice()));
        return productDTO;
    }

    public ProductDTO changeVisible(String uuid, ProductType type) {

        if (type.equals(ProductType.COUNTABLE)) return carpetService.changeVisible(uuid);

        return rugService.changeVisible(uuid);
    }

    public ProductDTO update(String uuid, ProductType type, ProductUpdateDTO dto) {

        if (type.equals(ProductType.COUNTABLE)) return carpetService.update(uuid, dto);

        return rugService.update(uuid, dto);
    }
}
