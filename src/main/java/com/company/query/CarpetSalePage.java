package com.company.query;

import java.time.LocalDateTime;

public interface CarpetSalePage {

    Integer getSaleId();
    LocalDateTime getCreatedDate();
    String getProductAttachId();
    String getPrice();
    String getProductName();
    Double getWeight();
    Double getHeight();
    Integer getAmount();
}
