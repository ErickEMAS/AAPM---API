package br.com.apm.domain.dto;

import lombok.Data;
import org.hibernate.id.GUIDGenerator;

import java.util.UUID;

@Data
public class SignUpDTO {

    private UUID id;
    private String email;
    private String cpf;
    private String fullName;
    private String nickName;
    private String password;
    private String passwordConfirm;

}
