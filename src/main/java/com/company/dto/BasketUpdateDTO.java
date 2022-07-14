package com.company.dto;

import com.company.enums.BasketStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasketUpdateDTO {

    private Integer basketId;
    private Double price;
    private BasketStatus status;
    private Integer amount;
    private Double height;

}
