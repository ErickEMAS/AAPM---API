package br.com.apm.domain.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ReactivateUserDTO {

    private UUID id;
    private String email;
    private String cpf;

}
