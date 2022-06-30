package com.company.dto.product;

import com.company.dto.factory.FactoryDTO;
import com.company.entity.FactoryEntity;
import com.company.enums.ProductStatus;
import com.company.enums.ProductType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO {

    private String uuid;
    private FactoryDTO factory;
    private String name;
    private String design;
    private String colour;
    private Double height;
    private Double weight;
    private String pon;
    private LocalDateTime createDate;
    private Integer amount;
    private ProductType type;
    private List<String> urlImageList;

    public ProductDTO(List<String> urlImageList) {
        this.urlImageList = urlImageList;
    }
}
