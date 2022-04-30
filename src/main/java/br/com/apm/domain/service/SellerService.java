package br.com.apm.domain.service;

import br.com.apm.domain.dto.*;
import br.com.apm.domain.models.*;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface SellerService {

    DynamicField addField(DynamicField dynamicField);
    Seller addSeller(SellerDTO sellerDTO);

    DynamicQuestionCheckList addQuestionChecklist(DynamicQuestionCheckList dynamicQuestionCheckList);

    CheckListVisita startChecklist(Seller seller);

    CheckListVisita answerChecklist(CheckListVisita checkListVisitaDTO);

    Seller getSeller(UUID sellerId);

    Page<Seller> getSellers(String nome, UUID tagId, Pageable pageable);

    Tag addTag(Tag tag);

    List<Tag> getTags();

    Seller addTagSeller(AddTagSellerDTO addTagSellerDTO);
}
