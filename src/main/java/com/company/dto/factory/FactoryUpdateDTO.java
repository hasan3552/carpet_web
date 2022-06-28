package com.company.dto.factory;

import com.company.enums.FactoryStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class FactoryUpdateDTO {

    @NotBlank
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    private FactoryStatus status;
    @NotBlank
    private Boolean visible;
}
