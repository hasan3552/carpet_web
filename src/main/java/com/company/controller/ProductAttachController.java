package com.company.controller;

import com.company.service.ProductAttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product_attach")
public class ProductAttachController {

    @Autowired
    private ProductAttachService productAttachService;


}
