package br.com.apm.application.controllers;

import br.com.apm.domain.dto.*;
import br.com.apm.domain.models.DynamicField;
import br.com.apm.domain.models.Seller;
import br.com.apm.domain.service.SellerService;
import br.com.apm.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @PostMapping("/add-field")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Object> addField(@RequestBody DynamicField dynamicField) {
        try {
            return ResponseEntity.ok(sellerService.addField(dynamicField));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/add-seller")
    public ResponseEntity<Object> addSeller(@RequestBody SellerDTO sellerDTO) {
        try {
            return ResponseEntity.ok(sellerService.addSeller(sellerDTO));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}

