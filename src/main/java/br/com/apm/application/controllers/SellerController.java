package br.com.apm.application.controllers;

import br.com.apm.domain.dto.*;
import br.com.apm.domain.models.*;
import br.com.apm.domain.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @PostMapping("/update-question-checklist")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Object> updateQuestionChecklist(@RequestBody DynamicQuestionCheckList dynamicQuestionCheckList) {
        try {
            return ResponseEntity.ok(sellerService.updateQuestionChecklist(dynamicQuestionCheckList));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/get-carteira-without_owner")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Object> getCarteiraWithoutOwner(Pageable pageable) {
        try {
            return ResponseEntity.ok(sellerService.getCarteiraWithoutOwner(pageable));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/tranfer-carteira")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Object> tranferCarteira(@RequestBody TranferCarteiraDTO tranferCarteiraDTO) {
        try {
            return ResponseEntity.ok(sellerService.tranferCarteira(tranferCarteiraDTO));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/get-fields")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Object> getFields(Pageable pageable) {
        try {
            return ResponseEntity.ok(sellerService.getFields(pageable));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/get-questions")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Object> getQuestions(@RequestParam(value = "status", required = false) String status,
                                               Pageable pageable) {
        try {
            return ResponseEntity.ok(sellerService.getQuestions(status, pageable));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/add-faq")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Object> addFAQ(@RequestBody FAQ faq) {
        try {
            return ResponseEntity.ok(sellerService.addFAQ(faq));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/update-faq")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Object> updateFAQ(@RequestBody FAQ faq) {
        try {
            return ResponseEntity.ok(sellerService.updateFAQ(faq));
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

    @PostMapping("/update-seller")
    public ResponseEntity<Object> updateSeller(@RequestBody Seller seller) {
        try {
            return ResponseEntity.ok(sellerService.updateSeller(seller));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/set-visit-itinerary")
    public ResponseEntity<Object> setVisitItinerary(@RequestBody List<Seller> sellers) {
        try {
            return ResponseEntity.ok(sellerService.setVisitItinerary(sellers));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/visiting")
    public ResponseEntity<Object> sellerVisiting(@RequestBody Seller seller) {
        try {
            return ResponseEntity.ok(sellerService.sellerVisiting(seller));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/reset-visit-itinerary")
    public ResponseEntity<Object> resetVisitItinerary() {
        try {
            return ResponseEntity.ok(sellerService.resetVisitItinerary());
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

    @GetMapping("/get-activity")
    public ResponseEntity<Object> getActivity(@RequestParam("sellerId") UUID sellerId) {
        try {
            return ResponseEntity.ok(sellerService.getActivity(sellerId));
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

    @GetMapping("/get-sellers")
    public ResponseEntity<Object> getSellers(@RequestParam(value = "nome", required = false) String nome,
                                             @RequestParam(value = "tagId", required = false) UUID tagId,
                                             Pageable pageable) {
        try {
            return ResponseEntity.ok(sellerService.getSellers(nome, tagId, pageable));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/add-tag")
    public ResponseEntity<Object> addTag(@RequestBody Tag tag) {
        try {
            return ResponseEntity.ok(sellerService.addTag(tag));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/delete-tag")
    public ResponseEntity<Object> deleteTag(@RequestBody Tag tag) {
        try {
            return ResponseEntity.ok(sellerService.deleteTag(tag));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/update-tag")
    public ResponseEntity<Object> updateTag(@RequestBody Tag tag) {
        try {
            return ResponseEntity.ok(sellerService.updateTag(tag));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/get-tags")
    public ResponseEntity<Object> getTags() {
        try {
            return ResponseEntity.ok(sellerService.getTags());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/add-tag-seller")
    public ResponseEntity<Object> addTagSeller(@RequestBody AddTagSellerDTO addTagSellerDTO) {
        try {
            return ResponseEntity.ok(sellerService.addTagSeller(addTagSellerDTO));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/get-faqs")
    public ResponseEntity<Object> getFAQS(@RequestParam(value = "search", required = false) String search,
                                          Pageable pageable) {
        try {
            return ResponseEntity.ok(sellerService.getFAQS(search, pageable));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/add-hunting")
    public ResponseEntity<Object> addHunting(@RequestBody Hunting hunting) {
        try {
            return ResponseEntity.ok(sellerService.addHunting(hunting));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/update-hunting")
    public ResponseEntity<Object> updateHunting(@RequestBody Hunting hunting) {
        try {
            return ResponseEntity.ok(sellerService.updateHunting(hunting));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/get-huntings")
    public ResponseEntity<Object> getHuntings(@RequestParam(value = "nome", required = false) String nome,
                                          Pageable pageable) {
        try {
            return ResponseEntity.ok(sellerService.getHuntings(nome, pageable));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

}

