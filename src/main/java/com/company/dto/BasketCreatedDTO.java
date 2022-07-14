package com.company.dto;

import com.company.enums.ProductType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasketCreatedDTO {

    private Integer giveProfile;
    private String productId;
    private ProductType type;
    private Integer amount;
    private String info;

}
