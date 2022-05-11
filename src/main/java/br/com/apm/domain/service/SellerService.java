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

    Page<Seller> getSellers(String search, UUID tagId, Pageable pageable);

    Tag addTag(Tag tag);

    Tag deleteTag(Tag tag);

    Tag updateTag(Tag tag);

    List<Tag> getTags();

    Seller addTagSeller(AddTagSellerDTO addTagSellerDTO);

    Seller updateSeller(Seller seller);

    Page<Carteira> getCarteiraWithoutOwner(Pageable pageable);

    Carteira tranferCarteira(TranferCarteiraDTO tranferCarteiraDTO);

    Page<DynamicField> getFields(Pageable pageable);

    Page<DynamicQuestionCheckList> getQuestions(String status, Pageable pageable);

    FAQ addFAQ(FAQ faq);

    FAQ updateFAQ(FAQ faq);

    Page<FAQ> getFAQS(String search, Pageable pageable);

    Hunting addHunting(Hunting hunting);

    Hunting updateHunting(Hunting hunting);

    Page<Hunting> getHuntings(String nome, Pageable pageable);

    DynamicQuestionCheckList updateQuestionChecklist(DynamicQuestionCheckList dynamicQuestionCheckList);

    List<Seller> setVisitItinerary(List<Seller> sellers);

    List<Seller> sellerVisiting(Seller seller);

    List<Seller> resetVisitItinerary();

    List<CheckListVisita> getActivity(UUID sellerId);

    String addSellerList(List<SellerDTO> sellerDTOS);

    Carteira getCarteira(UUID agenteId);
}
