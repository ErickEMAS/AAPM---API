package br.com.apm.domain.dto;

import br.com.apm.domain.enums.CodeType;
import lombok.Data;

@Data
public class SellerDTO {

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

}
