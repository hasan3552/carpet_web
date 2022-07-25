package com.company.service;

import com.company.entity.ProductAttachEntity;
import com.company.entity.ProductEntity;
import com.company.enums.AttachStatus;
import com.company.repository.ProductAttachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductAttachService {

    @Autowired
    private ProductAttachRepository productAttachRepository;

    @Value("${server.url}")
    private String serverUrl;


    public List<String> getProductAttachUrl(ProductEntity product){

        List<ProductAttachEntity> list = productAttachRepository
                .findAllByProductAndStatusAndVisible(product, AttachStatus.ACTIVE, Boolean.TRUE);

        List<String> urlList = new ArrayList<>();
        list.forEach(productAttachEntity -> urlList.add((serverUrl + "attach/open?fileId=" + productAttachEntity.getAttach().getUuid())));

        return urlList;
    }

}
