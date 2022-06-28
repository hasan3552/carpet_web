package com.company.dto.product;

import com.company.enums.ProductType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreateDTO {

    private Integer factoryId;
    private String name;
    private String design;
    private String colour;
    private Double height;
    private Double weight;
    private ProductType type;
    //    private String photo;
    private String pon;
    private Integer amount;
}
