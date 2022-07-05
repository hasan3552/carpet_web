package com.company.controller;

import com.company.dto.product.ProductCreateDTO;
import com.company.dto.product.ProductDTO;
import com.company.dto.product.ProductPageDTO;
import com.company.dto.product.ProductUpdateDTO;
import com.company.enums.ProductType;
import com.company.enums.ProfileRole;
import com.company.exp.NoPermissionException;
import com.company.service.ProductService;
import com.company.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/emp")
    public ResponseEntity<?> create(@RequestBody @Valid ProductCreateDTO dto) {

        ProductDTO productDTO = productService.create(dto);
        return ResponseEntity.ok(productDTO);

    }

    @GetMapping("/public/pagination/{type}")
    public ResponseEntity<?> paginationForPublic(@RequestParam("page") Integer page,
                                                 @RequestParam("size") Integer size,
                                                 @PathVariable("type") ProductType type) {

        List<ProductPageDTO> pagination = productService.pagination(page, size, type);
        return ResponseEntity.ok(pagination);

    }

    //
    @GetMapping("/adm/pagination/{type}")
    public ResponseEntity<?> paginationForAdmin(@RequestParam("page") Integer page,
                                                @RequestParam("size") Integer size,
                                                @PathVariable("type") ProductType type) {

        List<ProductPageDTO> pagination = productService.paginationForAdmin(page, size, type);
        return ResponseEntity.ok(pagination);

    }

    @GetMapping("/adm/{type}")
    public ResponseEntity<?> getProductForAdmin(@RequestParam("id") String uuid,
                                                @PathVariable("type") ProductType type) {

//        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        ProductDTO product = productService.getProduct(uuid, type);

        return ResponseEntity.ok(product);
    }

    @GetMapping("/public/{type}")
    public ResponseEntity<?> getProduct(@RequestParam("id") String uuid,
                                        @PathVariable("type") ProductType type) {

        //HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        ProductDTO product = productService.getProductForPublic(uuid, type);

        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/adm/{type}")
    public ResponseEntity<?> deleted(@RequestParam("id") String uuid,
                                     @PathVariable("type") ProductType type) {

        //    HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        ProductDTO productDTO = productService.changeVisible(uuid, type);
        return ResponseEntity.ok(productDTO);
    }

    @PutMapping("/emp/{type}")
    public ResponseEntity<?> update(@RequestParam("id") String uuid,
                                    @PathVariable("type") ProductType type,
                                    @RequestBody ProductUpdateDTO dto) {

        //   HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        ProductDTO update = productService.update(uuid, type, dto);
        return ResponseEntity.ok(update);
    }
}
