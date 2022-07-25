package com.company.service;

import com.company.dto.*;
import com.company.dto.basket.BasketCreatedDTO;
import com.company.dto.basket.BasketDTO;
import com.company.dto.basket.BasketShortDTO;
import com.company.dto.basket.BasketUpdateDTO;
import com.company.dto.sale.SaleCreateDTO;
import com.company.entity.BasketEntity;
import com.company.entity.CarpetEntity;
import com.company.enums.BasketStatus;
import com.company.enums.ProductType;
import com.company.exp.BadRequestException;
import com.company.exp.ItemNotFoundException;
import com.company.repository.BasketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BasketService {

    @Autowired
    private BasketRepository basketRepository;
    @Autowired
    private CarpetService carpetService;
    @Autowired
    private RugService rugService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ProductService productService;
    @Autowired
    private SaleService saleService;

    public BasketDTO created(BasketCreatedDTO dto) {

        if (!dto.getType().equals(ProductType.COUNTABLE) && !dto.getType().equals(ProductType.UNCOUNTABLE)) {
            throw new BadRequestException("no result");
        }

        if (dto.getType().equals(ProductType.COUNTABLE)) {

            CarpetEntity carpet = carpetService.get(dto.getProductId());

            if (dto.getAmount() < 0 || carpet.getAmount() < dto.getAmount()) {
                throw new BadRequestException("Not enough");
            }

        }

        BasketEntity basket = new BasketEntity();
        basket.setAmount(dto.getAmount());
        basket.setGiveProfile(profileService.getProfile());
        basket.setInfo(dto.getInfo());
        basket.setType(dto.getType());
        basket.setProductId(dto.getProductId());
        basketRepository.save(basket);

        return getBasketDTO(basket);
    }

    private BasketDTO getBasketDTO(BasketEntity basket) {

        BasketDTO dto = new BasketDTO();
        dto.setId(basket.getId());
        dto.setCreatedDate(basket.getCreatedDate());
        dto.setReturnedDate(basket.getReturnedDate());
        dto.setAmount(basket.getAmount());
        dto.setInfo(basket.getInfo());
        dto.setStatus(basket.getStatus());
        dto.setType(basket.getType());
        dto.setVisible(basket.getVisible());
        dto.setGetProfile(profileService.getShortDTO(basket.getGetProfile()));
        dto.setGiveProfile(profileService.getShortDTO(basket.getGiveProfile()));

        return dto;
    }

    public BasketDTO update(BasketUpdateDTO dto) {

        BasketEntity basket = get(dto.getBasketId());
        basket.setStatus(dto.getStatus());

        String productId = basket.getProductId();

        if (dto.getStatus().equals(BasketStatus.SOLD)) {

            SaleCreateDTO dto1 = new SaleCreateDTO();
            dto1.setAmount(basket.getAmount());
            dto1.setHeight(dto.getHeight());
            dto1.setPrice(dto.getPrice());
            dto1.setType(basket.getType());
            dto1.setProductId(productId);
            saleService.create(dto1);
            basket.setGetProfile(profileService.getProfile());
            basket.setReturnedDate(LocalDateTime.now());

        } else if (dto.getStatus().equals(BasketStatus.RETURNED)){

            basket.setGetProfile(profileService.getProfile());
            basket.setReturnedDate(LocalDateTime.now());

        }else if (dto.getStatus().equals(BasketStatus.GIVEN)) {

            basket.setAmount(dto.getAmount());

        }else {
            throw new BadRequestException("no result");
        }

        basketRepository.save(basket);
        return getBasketDTO(basket);
    }

    public BasketEntity get(Integer id) {

        return basketRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException("basket not fount");
        });
    }

    public ResponseInfoDTO changeVisible(Integer id) {

        BasketEntity basket = get(id);
        basket.setVisible(!basket.getVisible());
        basketRepository.save(basket);

        return new ResponseInfoDTO(1,"success");
    }

    public List<BasketShortDTO> getListByStatus(BasketStatus status) {

        List<BasketEntity> list = basketRepository.findAllByStatusAndVisible(status, Boolean.TRUE);

        List<BasketShortDTO> shortDTOS = new ArrayList<>();
        list.forEach(basketEntity -> shortDTOS.add(getBasketShortDTO(basketEntity)));

        return shortDTOS;
    }

    public BasketShortDTO getBasketShortDTO(BasketEntity basketEntity) {

        BasketShortDTO dto = new BasketShortDTO();
        dto.setCreatedDate(basketEntity.getCreatedDate());
        dto.setType(basketEntity.getType());
        dto.setId(basketEntity.getId());
        dto.setVisible(basketEntity.getVisible());

        if (basketEntity.getType().equals(ProductType.COUNTABLE)) {
            dto.setProduct(productService.getPageDTO(carpetService.get(basketEntity.getProductId())));
        } else {
            dto.setProduct(productService.getPageDTO(rugService.get(basketEntity.getProductId())));
        }
        dto.setStatus(basketEntity.getStatus());

        return dto;
    }

    public BasketDTO getBasketById(Integer id) {

        BasketEntity basket = get(id);
        return getBasketDTO(basket);
    }

    public List<BasketShortDTO> pagination(BasketStatus status, Integer page, Integer size) {

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<BasketEntity> all =  basketRepository.findAllByVisibleAndStatus(pageable, Boolean.TRUE, status);

        List<BasketEntity> list = all.getContent();

        List<BasketShortDTO> shortDTOS = new ArrayList<>();
        list.forEach(basketEntity -> shortDTOS.add(getBasketShortDTO(basketEntity)));

        return shortDTOS;
    }
}
