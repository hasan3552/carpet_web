package com.company.controller;

import com.company.dto.product.ProductCreateDTO;
import com.company.dto.product.ProductDTO;
import com.company.dto.product.ProductPageDTO;
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
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    @Lazy
    private ProductService productService;
    @Autowired
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

    @GetMapping("/public/pagination/{type}")
    public ResponseEntity<?> paginationForPublic(@RequestParam("page") Integer page,
                                                 @RequestParam("size") Integer size,
                                                 @PathVariable("type") ProductType type) {

        List<ProductPageDTO> pagination = productService.pagination(page, size, type);
        return ResponseEntity.ok(pagination);

    }

    @GetMapping("/adm/pagination/{type}")
    public ResponseEntity<?> paginationForAdmin(HttpServletRequest request,
                                                @RequestParam("page") Integer page,
                                                @RequestParam("size") Integer size,
                                                @PathVariable("type") ProductType type) {

        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        List<ProductPageDTO> pagination = productService.paginationForAdmin(page, size, type);
        return ResponseEntity.ok(pagination);

    }

    @GetMapping("/adm/{type}")
    public ResponseEntity<?> getProductForAdmin(HttpServletRequest request,
                                                @RequestParam("id") String uuid,
                                                @PathVariable("type") ProductType type) {

        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
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


}
