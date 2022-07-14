package com.company.dto.sale;

import com.company.enums.ProductType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleUpdateDTO {

    //private ProductType type;
    //private String productId;
    private Double price;
    private Integer amount;
    private Double height;
}
