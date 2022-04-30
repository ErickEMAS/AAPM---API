package br.com.apm.domain.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class AddTagSellerDTO {

    private UUID sellerId;
    private UUID tagId;

}
