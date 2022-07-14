package com.company.controller;

import com.company.dto.*;
import com.company.enums.BasketStatus;
import com.company.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/basket")
public class BasketController {

    @Autowired
    private BasketService basketService;

    @PostMapping("/emp/created")
    public ResponseEntity<?> create(@RequestBody BasketCreatedDTO dto){

        BasketDTO created = basketService.created(dto);
        return ResponseEntity.ok(created);

    }

    @PutMapping("/emp/update")
    public ResponseEntity<?> update(@RequestBody BasketUpdateDTO dto){

        BasketDTO update = basketService.update(dto);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/adm/{id}")
    public ResponseEntity<?> deleted(@PathVariable("id") Integer id){

        ResponseInfoDTO dto = basketService.changeVisible(id);
        return ResponseEntity.ok(dto);

    }

    @GetMapping("/emp/list/{status}")
    public ResponseEntity<?> getGivenList(@PathVariable("status") BasketStatus status){

        List<BasketShortDTO> dtoList = basketService.getListByStatus(status);
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/emp/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer id){

        //basket full dto
        BasketDTO dto = basketService.getBasketById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/emp/pagination/{status}")
    public ResponseEntity<?> getPlaylistByStatus(@PathVariable("status") BasketStatus status,
                                                 @RequestParam("page") Integer page,
                                                 @RequestParam("size") Integer size){

        //basket short dto pagination
        List<BasketShortDTO> pagination = basketService.pagination(status, page, size);
        return ResponseEntity.ok(pagination);
    }

}
