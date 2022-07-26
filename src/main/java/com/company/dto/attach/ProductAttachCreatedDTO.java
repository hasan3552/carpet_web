package com.company.dto.attach;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ProductAttachCreatedDTO {

    private List<MultipartFile> files;
    private String productId;

}
