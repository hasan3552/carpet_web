package com.company.dto.product;

import com.company.enums.ProductType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@ToString
public class ProductCreateDTO {

    private Integer factoryId;
    private ProductType type;
    private Double price;
    @NotBlank
    private String name;
    @NotBlank
    private String design;
    @NotBlank
    private String colour;
    @NotBlank
    private String pon;
    private Double height;
    private Double weight;
    private Integer amount;
}
