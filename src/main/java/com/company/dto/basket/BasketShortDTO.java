package com.company.dto.basket;

import com.company.dto.product.ProductPageDTO;
import com.company.dto.profile.ProfileShortDTO;
import com.company.enums.BasketStatus;
import com.company.enums.ProductType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BasketShortDTO {

    private Integer id;
    private ProductPageDTO product;
    private ProductType type;
    private LocalDateTime createdDate;
    private BasketStatus status;
    private Boolean visible;

}
