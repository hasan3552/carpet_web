package com.company.controller;

import com.company.dto.ResponseDTO;
import com.company.dto.sale.SaleCreateDTO;
import com.company.enums.ProductType;
import com.company.enums.ProfileRole;
import com.company.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/sale")
public class SaleController {

    @Autowired
    private SaleService saleService;


    @PostMapping("/emp/create")
    public ResponseEntity<?> create(@RequestBody SaleCreateDTO dto) {

        ResponseDTO responseDTO = saleService.create(dto);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/adm/{type}")
    public ResponseEntity<?> changeVisible(@PathVariable("type") ProductType type,
                                           @RequestParam("id") Integer id) {
        // HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        ResponseDTO responseDTO = saleService.changeVisible(id, type);

        return ResponseEntity.ok(responseDTO);
    }
}
