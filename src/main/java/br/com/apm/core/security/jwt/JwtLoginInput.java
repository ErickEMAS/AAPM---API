package br.com.apm.core.security.jwt;

import lombok.Data;

@Data
class JwtLoginInput {
    private String email;
    private String password;
}
