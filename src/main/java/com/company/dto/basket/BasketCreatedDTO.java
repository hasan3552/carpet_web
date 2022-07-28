package com.company.dto.basket;

import com.company.enums.ProductType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasketCreatedDTO {

    private String productId;
    private ProductType type;
    private Integer amount;
    private String info;

}
