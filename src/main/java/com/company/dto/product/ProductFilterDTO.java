package com.company.dto.product;

import com.company.enums.ProductStatus;
import com.company.enums.ProductType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProductFilterDTO {

    private String uuid;
    private String factoryName;
    private String name;
    private String design;
    private String colour;
    private Double height;
    private Double weight;
    private String pon;
    private String publishedDateTo;
    private String publishedDateFrom;
    private ProductType type;
    private ProductStatus status;

}
