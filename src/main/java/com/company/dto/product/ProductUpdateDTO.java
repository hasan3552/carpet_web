package com.company.dto.product;

import com.company.enums.ProductType;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductUpdateDTO {

    private Integer factoryId;
    private String name;
    private String design;
    private String colour;
    private Double height;
    private Double weight;
    private String pon;
    private Integer amount;
    private ProductType type;
    private Double price;


}
