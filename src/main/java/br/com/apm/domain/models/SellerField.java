package br.com.apm.domain.models;

import br.com.apm.domain.enums.TypeField;
import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
public class SellerField {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID idDynamicSellerRef;
    private String name;
    private String value;
    private TypeField type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seller_id")
    private Seller seller;

}
