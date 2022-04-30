package br.com.apm.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "carteira_id")
    private Carteira carteira;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "seller_tags",
            joinColumns = @JoinColumn(name = "seller_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    @Fetch(FetchMode.SUBSELECT)
    private List<Tag> tags;

}
