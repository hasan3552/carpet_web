package com.company.dto.product;

import com.company.enums.ProductType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ProductCreateDTO {

    private Integer factoryId;
    private ProductType type;

    private String name;
    private String design;
    private String colour;
    private String pon;
    private Double height;
    private Double weight;

    private Integer amount;
}
