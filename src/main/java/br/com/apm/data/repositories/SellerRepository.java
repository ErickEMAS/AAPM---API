package br.com.apm.data.repositories;

import br.com.apm.domain.models.Seller;
import br.com.apm.domain.models.SellerField;
import br.com.apm.domain.models.UserAPI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SellerRepository extends JpaRepository<Seller, UUID> {

    Page<Seller> findByCarteira_idAndNomeContainingIgnoreCase(UUID carteiraId, String nome,  Pageable pageable);
    Page<Seller> findByTags_idAndNomeContainingIgnoreCase(UUID tagId, String nome, Pageable pageable);

    Page<Seller> findAll(Pageable pageable);

}
