package com.company.dto.product;

import com.company.dto.factory.FactoryDTO;
import com.company.entity.FactoryEntity;
import com.company.enums.ProductStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProductDTO {

    private String uuid;
    private FactoryDTO factory;
    private String name;
    private String design;
    private String colour;
    private Double height;
    private Double weight;
//    private String photo;
    private String pon;
    private ProductStatus status;
    private LocalDateTime createDate;
    private Integer amount;
    private Boolean visible;

}
