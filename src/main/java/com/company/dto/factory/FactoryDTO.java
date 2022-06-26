package com.company.dto.factory;

import com.company.enums.FactoryStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FactoryDTO {

    private Integer id;
    private String name;
    private String key;
    private LocalDateTime createdDate;
    private FactoryStatus status;
    private Boolean visible;
    private String photoUrl;

}
