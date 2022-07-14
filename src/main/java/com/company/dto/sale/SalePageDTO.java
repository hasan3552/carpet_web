package com.company.dto.sale;

import com.company.enums.ProductType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SalePageDTO {

    private Integer saleId;
    private LocalDateTime createdDate;
    private String productAttachUrl;
    private Double price;
    private String productName;
    private Integer amount;
    private Double weight;
    private Double height;
    private ProductType type;

}
