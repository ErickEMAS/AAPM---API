package br.com.apm.domain.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class TranferCarteiraDTO {

    private UUID userId;
    private UUID carteiraId;

}
