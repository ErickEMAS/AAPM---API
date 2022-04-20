package br.com.apm.domain.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ConfirmCodeDTO {

    private UUID userId;
    private String code;
    private int codeType;

}
