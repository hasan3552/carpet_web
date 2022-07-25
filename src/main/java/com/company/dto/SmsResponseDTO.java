package com.company.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SmsResponseDTO {

    private Boolean success;
    private String reason;
    private Integer resultCode;

}