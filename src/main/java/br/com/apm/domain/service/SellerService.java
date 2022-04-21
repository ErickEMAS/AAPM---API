package br.com.apm.domain.service;

import br.com.apm.domain.dto.*;
import br.com.apm.domain.models.DynamicField;
import br.com.apm.domain.models.Seller;

import java.util.UUID;

public interface SellerService {

    DynamicField addField(DynamicField dynamicField);
    Seller addSeller(SellerDTO sellerDTO);

}
