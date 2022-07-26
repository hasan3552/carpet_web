package com.company.controller;

import com.company.dto.ResponseDTO;
import com.company.dto.ResponseInfoDTO;
import com.company.dto.SearchByDate;
import com.company.dto.sale.SaleCreateDTO;
import com.company.dto.sale.SaleDTO;
import com.company.dto.sale.SalePageDTO;
import com.company.dto.sale.SaleUpdateDTO;
import com.company.enums.ProductType;
import com.company.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        ResponseDTO responseDTO = saleService.changeVisible(id, type);
        return ResponseEntity.ok(responseDTO);

    }

    @PutMapping("/emp/update/{type}")
    public ResponseEntity<?> update(@PathVariable ProductType type,
                                    @RequestParam("id") Integer id,
                                    @RequestBody SaleUpdateDTO dto) {

        ResponseInfoDTO update = saleService.update(type, id, dto);
        return ResponseEntity.ok(update);

    }

    @GetMapping("/emp/pagination/{type}")
    public ResponseEntity<?> paginationForAdmin(@RequestParam("page") Integer page,
                                                @RequestParam("size") Integer size,
                                                @PathVariable("type") ProductType type) {

        List<SalePageDTO> pagination = saleService.pagination(page, size, type);
        return ResponseEntity.ok(pagination);

    }





    @PostMapping("/adm/created_date")
    public ResponseEntity<?> listByCreatedDate(@RequestBody SearchByDate search){

        List<SaleDTO> saleDTOS = saleService.searchByCreatedDate(search);
        return ResponseEntity.ok(saleDTOS);

    }
}
