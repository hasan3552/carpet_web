package com.company.controller;

import com.company.dto.product.ProductCreateDTO;
import com.company.dto.product.ProductDTO;
import com.company.service.CarpetService;
import com.company.service.RugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private CarpetService carpetService;
    @Autowired
    private RugService rugService;

    @PostMapping("")
    public ProductDTO create(@RequestHeader("Authorization") String jwt,
                             @RequestBody ProductCreateDTO dto){

        return null;
    }
}
