package br.com.apm.data.repositories;

import br.com.apm.domain.models.SellerField;
import br.com.apm.domain.models.UserAPI;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SellerFieldRepository extends JpaRepository<SellerField, UUID> {

    SellerField findByIdDynamicSellerRef(UUID idDynamicSellerRef);

}
