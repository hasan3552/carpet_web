package com.company.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
@Getter
@Setter
public class AttachDTO {
    private Integer id;
    private String fileName;
    private String fileType;
    private byte[] fileData;
    private LocalDateTime createdDate;
    private Boolean visible ;
}
