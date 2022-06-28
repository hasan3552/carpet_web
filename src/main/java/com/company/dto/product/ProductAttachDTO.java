package com.company.dto.product;

import com.company.enums.ProductType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ProductAttachDTO {

    @NotBlank
    private ProductType type;
    @NotBlank
    private String productId;

}
