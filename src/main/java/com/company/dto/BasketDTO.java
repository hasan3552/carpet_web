package com.company.dto;

import com.company.dto.product.ProductPageDTO;
import com.company.dto.profile.ProfileShortDTO;
import com.company.enums.BasketStatus;
import com.company.enums.ProductType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BasketDTO {

    private Integer id;
    private ProfileShortDTO giveProfile;
    private ProfileShortDTO getProfile;
    private ProductPageDTO product;
    private ProductType type;
    private Integer amount;
    private LocalDateTime createdDate;
    private LocalDateTime returnedDate;
    private String info;
    private BasketStatus status;
    private Boolean visible;

}
