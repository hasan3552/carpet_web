package com.company.controller;

import com.company.dto.product.ProductCreateDTO;
import com.company.dto.product.ProductDTO;
import com.company.enums.ProductType;
import com.company.enums.ProfileRole;
import com.company.exp.BadRequestException;
import com.company.exp.NoPermissionException;
import com.company.service.CarpetService;
import com.company.service.ProductService;
import com.company.service.ProfileService;
import com.company.service.RugService;
import com.company.util.HttpHeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    @Lazy
    private ProfileService profileService;

    @PostMapping("/adm")
    public ResponseEntity<?> create(HttpServletRequest request,
                                    @RequestBody ProductCreateDTO dto) {

        Integer profileId = HttpHeaderUtil.getId(request);
        if (profileService.get(profileId).getRole().equals(ProfileRole.CUSTOMER)) {
            throw new NoPermissionException("No access");
        }

        ProductDTO productDTO = productService.create(profileId, dto);
        return ResponseEntity.ok(productDTO);
    }

    @GetMapping("/pagination/{type}")
    public ResponseEntity<?> pagination(@RequestParam("page") Integer page,
                                        @RequestParam("size") Integer size,
                                        @PathVariable("type") ProductType type) {

        productService.pagination(page, size, type);
        return null;
    }

}
