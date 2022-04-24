package br.com.apm.application.controllers;

import br.com.apm.domain.dto.*;
import br.com.apm.domain.models.CheckListVisita;
import br.com.apm.domain.models.DynamicField;
import br.com.apm.domain.models.DynamicQuestionCheckList;
import br.com.apm.domain.models.Seller;
import br.com.apm.domain.service.SellerService;
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

    @PostMapping("/add-question-checklist")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Object> addQuestionChecklist(@RequestBody DynamicQuestionCheckList dynamicQuestionCheckList) {
        try {
            return ResponseEntity.ok(sellerService.addQuestionChecklist(dynamicQuestionCheckList));
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

    @PostMapping("/start-checklist-by-selerid")
    public ResponseEntity<Object> startChecklist(@RequestBody Seller seller) {
        try {
            return ResponseEntity.ok(sellerService.startChecklist(seller));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/answer-checklist")
    public ResponseEntity<Object> answerChecklist(@RequestBody CheckListVisita checkListVisita) {
        try {
            return ResponseEntity.ok(sellerService.answerChecklist(checkListVisita));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/get-seller")
    public ResponseEntity<Object> getSeller(@RequestParam("sellerId") UUID sellerId) {
        try {
            return ResponseEntity.ok(sellerService.getSeller(sellerId));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

}

