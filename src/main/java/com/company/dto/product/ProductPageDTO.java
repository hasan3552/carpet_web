package com.company.dto.product;

import com.company.enums.ProductType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductPageDTO {

    private String uuid;
    private LocalDateTime createdDate;
    private Double weight;
    private Double height;
    private String factoryName;
    private String factoryAttachUrl;
    private List<String> imageUrlList;
    private String name;
    private Double price;
//    private ProductType type;
}
