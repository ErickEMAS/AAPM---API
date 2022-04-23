package br.com.apm.domain.models;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<SellerField> sellerFields;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<CheckListVisita> checkListVisitas;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "carteira_id")
    private Carteira carteira;

}
