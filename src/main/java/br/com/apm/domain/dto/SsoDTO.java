package br.com.apm.domain.dto;

import br.com.apm.domain.models.UserAPI;
import lombok.Data;

@Data
public class SsoDTO {

    private String access_token;
    private UserDTO me;

}
