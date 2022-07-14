package com.company.service;

import com.company.dto.ResponseDTO;
import com.company.dto.ResponseInfoDTO;
import com.company.dto.product.ProductPageDTO;
import com.company.dto.sale.SaleCreateDTO;
import com.company.dto.sale.SalePageDTO;
import com.company.dto.sale.SaleUpdateDTO;
import com.company.entity.*;
import com.company.enums.ProductStatus;
import com.company.enums.ProductType;
import com.company.enums.ProfileRole;
import com.company.exp.BadRequestException;
import com.company.exp.ItemNotFoundException;
import com.company.repository.SaleCarpetRepository;
import com.company.repository.SaleRugRepository;
import com.company.util.CurrencyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService {

    @Autowired
    private SaleCarpetRepository saleCarpetRepository;

    @Autowired
    private SaleRugRepository saleRugRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private CarpetService carpetService;
    @Autowired
    private RugService rugService;
    @Autowired
    private ProductAttachService productAttachService;


    public ResponseDTO create(SaleCreateDTO dto) {

        ProfileEntity profile = profileService.getProfile();

        if (dto.getType().equals(ProductType.COUNTABLE)) {

            SaleCarpetEntity entity = new SaleCarpetEntity();
            entity.setAmount(dto.getAmount());
            CarpetEntity carpet = carpetService.get(dto.getProductId());
            entity.setCarpet(carpet);
            entity.setPrice(dto.getPrice() / CurrencyUtil.getRate());
            entity.setProfile(profile);

            if (carpet.getAmount() < dto.getAmount()) {
//                throw new BadRequestException("amount not enough");10 12
                return new ResponseDTO(-1,"amount not enough");
            }

            carpet.setAmount(carpet.getAmount() - dto.getAmount());
            if (carpet.getAmount() == 0) {
                carpet.setStatus(ProductStatus.BLOCKED);
            }

            carpetService.save(carpet);
            saleCarpetRepository.save(entity);

            return new ResponseDTO(1, "success");
        }

        SaleRugEntity entity = new SaleRugEntity();
        entity.setHeight(dto.getHeight());
        entity.setPrice(dto.getPrice() / CurrencyUtil.getRate());
        entity.setProfile(profile);
        RugEntity rug = rugService.get(dto.getProductId());
        entity.setRug(rug);

        if (rug.getHeight() < dto.getHeight()) {
            return new ResponseDTO(-1,"height not enough");
        }

        rug.setHeight(rug.getHeight() - dto.getHeight());
        if (rug.getHeight() < 50) {
            rug.setStatus(ProductStatus.BLOCKED);
        }

        rugService.save(rug);
        saleRugRepository.save(entity);

        return new ResponseDTO(1, "success");
    }

    public ResponseDTO changeVisible(Integer saleId, ProductType type) {

        if (type.equals(ProductType.COUNTABLE)) {

            SaleCarpetEntity saleCarpet = getSaleCarpet(saleId);
            saleCarpet.setVisible(!saleCarpet.getVisible());

            saleCarpetRepository.save(saleCarpet);

            return new ResponseDTO(1, "success");
        }

        SaleRugEntity saleRug = getSaleRug(saleId);
        saleRug.setVisible(!saleRug.getVisible());

        saleRugRepository.save(saleRug);

        return new ResponseDTO(1, "success");

    }

    private SaleRugEntity getSaleRug(Integer saleId) {
        return saleRugRepository.findById(saleId).orElseThrow(() -> {
            throw new ItemNotFoundException("sale not fount");
        });
    }

    private SaleCarpetEntity getSaleCarpet(Integer saleId) {

        return saleCarpetRepository.findById(saleId).orElseThrow(() -> {
            throw new ItemNotFoundException("sale not fount");
        });
    }

    public ResponseInfoDTO update(ProductType type, Integer id, SaleUpdateDTO dto) {

        if (type.equals(ProductType.COUNTABLE)) {
            SaleCarpetEntity saleCarpet = getSaleCarpet(id);
            saleCarpet.setPrice(dto.getPrice() / CurrencyUtil.getRate());
            Integer oldAmount = saleCarpet.getAmount();
            saleCarpet.setAmount(dto.getAmount());

            CarpetEntity carpet = saleCarpet.getCarpet();
            carpet.setAmount(carpet.getAmount() + oldAmount - dto.getAmount());

            if (carpet.getAmount() < 0) {
                throw new BadRequestException("amount not enough");
            }

            if (carpet.getAmount() == 0) {
                carpet.setStatus(ProductStatus.BLOCKED);
            }

            if (carpet.getAmount() > 0) {
                carpet.setStatus(ProductStatus.ACTIVE);
            }

            carpetService.save(carpet);

            saleCarpetRepository.save(saleCarpet);

        } else if (type.equals(ProductType.UNCOUNTABLE)) {
            SaleRugEntity saleRug = getSaleRug(id);

            saleRug.setPrice(dto.getPrice() / CurrencyUtil.getRate());

            Double oldHeight = saleRug.getHeight();
            saleRug.setHeight(dto.getHeight());

            RugEntity rug = saleRug.getRug();
            rug.setHeight(rug.getHeight() + oldHeight - dto.getHeight());

            if (rug.getHeight() < 0) {
                throw new BadRequestException("height not enough");
            }

            if (rug.getHeight() < 50) {
                rug.setStatus(ProductStatus.BLOCKED);
            }

            if (rug.getHeight() > 50) {
                rug.setStatus(ProductStatus.ACTIVE);
            }

            rugService.save(rug);
            saleRugRepository.save(saleRug);

        }

        return new ResponseInfoDTO(1, "success");

    }

    public List<SalePageDTO> pagination(Integer page, Integer size, ProductType type) {

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        List<SalePageDTO> dtos = new ArrayList<>();

        if (type.equals(ProductType.COUNTABLE)) {

            Page<SaleCarpetEntity> all = saleCarpetRepository.findAll(pageable);
            List<SaleCarpetEntity> list = all.getContent();

            list.forEach(saleCarpetEntity -> {

                SalePageDTO dto = new SalePageDTO();
                dto.setSaleId(saleCarpetEntity.getId());
                dto.setCreatedDate(saleCarpetEntity.getCreatedDate());
                dto.setAmount(saleCarpetEntity.getAmount());
                dto.setHeight(saleCarpetEntity.getCarpet().getHeight());
                dto.setWeight(saleCarpetEntity.getCarpet().getWeight());
                dto.setPrice(saleCarpetEntity.getPrice() * CurrencyUtil.getRate());
                dto.setType(ProductType.COUNTABLE);
                dto.setProductName(saleCarpetEntity.getCarpet().getProduct().getName());
                dto.setProductAttachUrl(productAttachService.getProductAttachUrl(saleCarpetEntity
                        .getCarpet().getProduct()).get(0));

                dtos.add(dto);
            });

            return dtos;
           }

        Page<SaleRugEntity> all = saleRugRepository.findAll(pageable);
        List<SaleRugEntity> list = all.getContent();

        list.forEach(saleRugEntity -> {

            SalePageDTO dto = new SalePageDTO();
            dto.setSaleId(saleRugEntity.getId());
            dto.setCreatedDate(saleRugEntity.getCreatedDate());
            dto.setAmount(1);
            dto.setHeight(saleRugEntity.getHeight());
            dto.setWeight(saleRugEntity.getRug().getWeight());
            dto.setPrice(saleRugEntity.getPrice() * CurrencyUtil.getRate());
            dto.setType(ProductType.UNCOUNTABLE);
            dto.setProductName(saleRugEntity.getRug().getProduct().getName());
            dto.setProductAttachUrl(productAttachService.getProductAttachUrl(saleRugEntity
                    .getRug().getProduct()).get(0));

            dtos.add(dto);
        });

        return dtos;
    }
}
