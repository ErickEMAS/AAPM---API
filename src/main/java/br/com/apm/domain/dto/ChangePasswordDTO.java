package br.com.apm.domain.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ChangePasswordDTO {

    private String email;
    private String code;
    private String password;
    private String passwordConfirm;

}
