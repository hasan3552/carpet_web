package com.company.dto.media;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MediaDTO {

    private Integer id;
    private String fileName;
    private String fileType;
    private byte[] fileData;
    private LocalDateTime createdDate;
    private Boolean visible;

}
