package br.com.apm.domain.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ConfirmCodeDTO {

    private String email;
    private String code;
    private int codeType;

}
