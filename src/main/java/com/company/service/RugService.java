package com.company.service;

import com.company.dto.product.ProductCreateDTO;
import com.company.dto.product.ProductDTO;
import com.company.entity.*;
import com.company.enums.ProductStatus;
import com.company.enums.ProductType;
import com.company.repository.DetailRepository;
import com.company.repository.RugRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RugService {

    @Autowired
    private RugRepository rugRepository;
    public void create(ProductEntity product, ProductCreateDTO dto) {

        for (int i = 0; i < dto.getAmount(); i++) {

            RugEntity rug = new RugEntity();
            rug.setHeight(dto.getHeight());
            rug.setWeight(dto.getWeight());
            rug.setProduct(product);

            rugRepository.save(rug);
        }
    }

}
