package com.company.dto.sale;

import com.company.dto.factory.FactoryDTO;
import com.company.dto.product.ProductDTO;
import com.company.dto.profile.ProfileDTO;
import com.company.entity.CarpetEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ProductType;
import com.company.enums.SaleStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class SaleDTO {

    private Integer id;
    private ProfileDTO profile;
    private Integer amount;
    private LocalDateTime createdDate;
    private Double price;
    private FactoryDTO factoryDTO;
    private ProductDTO productDTO;
    private Double height;
    private SaleStatus status;

}
