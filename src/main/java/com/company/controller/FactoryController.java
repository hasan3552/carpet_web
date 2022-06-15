package com.company.controller;

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

    @PostMapping("")
    public ResponseEntity<?> create(@RequestHeader("Authorization") String jwt,
                                    @RequestBody String name){

        Integer profileId = JwtUtil.decode(jwt);
        FactoryDTO dto = factoryService.created(name, profileId);

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestHeader("Authorization") String jwt,
                                    @RequestBody String name,
                                    @PathVariable("id") Integer factoryId){

        Integer profileId = JwtUtil.decode(jwt);
        FactoryDTO dto = factoryService.update(name, profileId, factoryId);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Integer factoryId){

        FactoryDTO dto = factoryService.getFactory(factoryId);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("")
    public ResponseEntity<?> getList(){

        List<FactoryDTO> dtos = factoryService.getFactoryList();

        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> changeVisible(@PathVariable("id") Integer factoryId){

        FactoryDTO dto = factoryService.changeVisible(factoryId);
        return ResponseEntity.ok(dto);
    }
}
