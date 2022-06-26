package com.company.controller;

import com.company.dto.factory.FactoryCreateDTO;
import com.company.dto.factory.FactoryDTO;
import com.company.service.FactoryService;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/factory")
public class FactoryController {
    @Autowired
    private FactoryService factoryService;

    // --------------------  ADMIN  ------------------------------------
    @PostMapping("")
    public ResponseEntity<?> create(@RequestHeader("Authorization") String jwt,
                                    @RequestBody FactoryCreateDTO dto1){

        Integer profileId = JwtUtil.decode(jwt);
        FactoryDTO dto = factoryService.created(dto1, profileId);

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestHeader("Authorization") String jwt,
                                    @RequestBody FactoryCreateDTO dto1,
                                    @PathVariable("id") Integer factoryId){

        Integer profileId = JwtUtil.decode(jwt);
        FactoryDTO dto = factoryService.update(dto1, profileId, factoryId);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllList(@RequestHeader("Authorization") String jwt){

        Integer profileId = JwtUtil.decode(jwt);
        List<FactoryDTO> dtos = factoryService.getFactoryList(profileId);

        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> changeVisible(@RequestHeader("Authorization") String jwt,
                                           @PathVariable("id") Integer factoryId){

        Integer profileId = JwtUtil.decode(jwt);

        FactoryDTO dto = factoryService.changeVisible(factoryId, profileId);
        return ResponseEntity.ok(dto);
    }
    //----------------  PUBLIC  -----------------------------

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Integer factoryId){

        FactoryDTO dto = factoryService.getFactory(factoryId);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/public/list")
    public ResponseEntity<?> getAllListByVisibleAndStatus(){

        List<FactoryDTO> dtoList = factoryService.getAllListByStatusAndVisible();

        return ResponseEntity.ok(dtoList);
    }
}
