package com.company.controller;

import com.company.dto.factory.FactoryCreateDTO;
import com.company.dto.factory.FactoryDTO;
import com.company.dto.factory.FactoryUpdateDTO;
import com.company.enums.ProfileRole;
import com.company.service.FactoryService;
import com.company.util.HttpHeaderUtil;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/factory")
public class FactoryController {
    @Autowired
    private FactoryService factoryService;

    // --------------------  ADMIN  ------------------------------------
    @PostMapping("/adm")
    public ResponseEntity<?> create(HttpServletRequest request,
                                    @RequestBody @Valid FactoryCreateDTO dto1) {

        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        FactoryDTO dto = factoryService.created(dto1);

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/adm/{id}")
    public ResponseEntity<?> update(HttpServletRequest request,
                                    @RequestBody @Valid FactoryUpdateDTO dto1,
                                    @PathVariable("id") Integer factoryId) {

        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        FactoryDTO dto = factoryService.update(dto1, factoryId);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllList(@RequestHeader("Authorization") String jwt) {

        Integer profileId = JwtUtil.decode(jwt);
        List<FactoryDTO> dtos = factoryService.getFactoryList(profileId);

        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> changeVisible(@RequestHeader("Authorization") String jwt,
                                           @PathVariable("id") Integer factoryId) {

        Integer profileId = JwtUtil.decode(jwt);

        FactoryDTO dto = factoryService.changeVisible(factoryId, profileId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/adm/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Integer factoryId,
                                 HttpServletRequest request) {

        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);

        FactoryDTO dto = factoryService.getFactory(factoryId);

        return ResponseEntity.ok(dto);
    }

    //----------------  PUBLIC  -----------------------------
    @GetMapping("/{id}")
    public ResponseEntity<?> getPublic(@PathVariable("id") Integer factoryId) {

        FactoryDTO dto = factoryService.getFactory(factoryId);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/public/list")
    public ResponseEntity<?> getAllListByVisibleAndStatus() {

        List<FactoryDTO> dtoList = factoryService.getAllListByStatusAndVisible();

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/pagination")
    public ResponseEntity<?> pagination(@RequestParam("size") Integer size,
                                        @RequestParam("page") Integer page) {

        PageImpl pagination = factoryService.pagination(page, size);

        return ResponseEntity.ok(pagination);
    }


}
