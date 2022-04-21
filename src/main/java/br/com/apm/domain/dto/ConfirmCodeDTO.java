package br.com.apm.domain.dto;

import br.com.apm.domain.enums.CodeType;
import lombok.Data;

import java.util.UUID;

@Data
public class ConfirmCodeDTO {

    private String email;
    private String code;
    private CodeType codeType;

}
