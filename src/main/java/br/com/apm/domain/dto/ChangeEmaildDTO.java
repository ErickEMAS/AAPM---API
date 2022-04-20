package br.com.apm.domain.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ChangeEmaildDTO {

    private String code;
    private String newEmail;

}
