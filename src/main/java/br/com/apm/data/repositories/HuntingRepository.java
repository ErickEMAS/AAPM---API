package br.com.apm.data.repositories;

import br.com.apm.domain.models.Hunting;
import br.com.apm.domain.models.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HuntingRepository extends JpaRepository<Hunting, UUID> {

    Page<Hunting> findByUser_idAndNomeContainingIgnoreCase(UUID userId, String nome, Pageable pageable);
    Page<Hunting> findByUser_id(UUID userId, Pageable pageable);

}
