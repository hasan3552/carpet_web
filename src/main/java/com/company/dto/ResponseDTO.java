package com.company.dto;

import com.company.dto.product.ProductDTO;

public class ResponseDTO  {

    private Integer status;
    private String message;

    public ResponseDTO(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
