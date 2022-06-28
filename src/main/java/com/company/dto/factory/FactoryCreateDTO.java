package com.company.dto.factory;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class FactoryCreateDTO {

    @NotBlank
    private String name;
}
