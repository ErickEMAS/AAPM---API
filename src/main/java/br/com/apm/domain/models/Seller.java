package br.com.apm.domain.models;

import br.com.apm.domain.servicesImpl.SellerImpl;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String cnpj;
    private String helenaSellerCode;
    private String nome;
    private String telefone;
    private String email;
    private String cidade;
    private String uf;
    private String cep;
    private String endereco;
    private String numero;
    private String complemento;
    private String cadastro;
    private String dataPedidoTeste;

    @OneToMany(cascade = CascadeType.ALL)
    private List<SellerField> sellerFields;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "carteira_id")
    private Carteira carteira;

}
