package br.com.apm.domain.service;

import br.com.apm.domain.dto.*;
import br.com.apm.domain.models.*;
import org.aspectj.weaver.patterns.TypePatternQuestions;

import java.util.UUID;

public interface SellerService {

    DynamicField addField(DynamicField dynamicField);
    Seller addSeller(SellerDTO sellerDTO);

    DynamicQuestionCheckList addQuestionChecklist(DynamicQuestionCheckList dynamicQuestionCheckList);

    CheckListVisita startChecklist(Seller seller);

    CheckListVisita answerChecklist(CheckListVisita checkListVisitaDTO);
}
