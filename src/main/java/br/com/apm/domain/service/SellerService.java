package br.com.apm.domain.service;

import br.com.apm.domain.dto.*;
import br.com.apm.domain.models.DynamicField;
import br.com.apm.domain.models.DynamicQuestionCheckList;
import br.com.apm.domain.models.QuestionCheckList;
import br.com.apm.domain.models.Seller;
import org.aspectj.weaver.patterns.TypePatternQuestions;

import java.util.UUID;

public interface SellerService {

    DynamicField addField(DynamicField dynamicField);
    Seller addSeller(SellerDTO sellerDTO);

    DynamicQuestionCheckList addQuestionChecklist(DynamicQuestionCheckList dynamicQuestionCheckList);
}
