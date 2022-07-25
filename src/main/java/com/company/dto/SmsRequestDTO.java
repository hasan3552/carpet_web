package com.company.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SmsRequestDTO {

    private Integer id;
    private String key;
    private String phone;
    private String message;
}
