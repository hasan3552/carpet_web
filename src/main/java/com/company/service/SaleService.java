package com.company.service;

import com.company.dto.ResponseDTO;
import com.company.dto.sale.SaleCreateDTO;
import com.company.entity.ProfileEntity;
import com.company.entity.SaleCarpetEntity;
import com.company.entity.SaleRugEntity;
import com.company.enums.ProductType;
import com.company.enums.ProfileRole;
import com.company.exp.ItemNotFoundException;
import com.company.repository.SaleCarpetRepository;
import com.company.repository.SaleRugRepository;
import com.company.util.CurrencyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    public ResponseDTO create(SaleCreateDTO dto) {

        ProfileEntity profile = profileService.getProfile();

        if (dto.getType().equals(ProductType.COUNTABLE)) {

            SaleCarpetEntity entity = new SaleCarpetEntity();
            entity.setAmount(dto.getAmount());
            entity.setCarpet(carpetService.get(dto.getProductId()));
            entity.setPrice(dto.getPrice() / CurrencyUtil.getRate());
            entity.setProfile(profile);

            saleCarpetRepository.save(entity);

            return new ResponseDTO(1, "success");
        }

        SaleRugEntity entity = new SaleRugEntity();
        entity.setHeight(dto.getHeight());
        entity.setPrice(dto.getPrice() / CurrencyUtil.getRate());
        entity.setProfile(profile);
        entity.setRug(rugService.get(dto.getProductId()));

        saleRugRepository.save(entity);

        return new ResponseDTO(1, "success");
    }

    public ResponseDTO changeVisible(Integer saleId, ProductType type) {

        if (type.equals(ProductType.COUNTABLE)){

            SaleCarpetEntity saleCarpet = getSaleCarpet(saleId);
            saleCarpet.setVisible(!saleCarpet.getVisible());

            saleCarpetRepository.save(saleCarpet);

            return new ResponseDTO(1,"success");
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
}
