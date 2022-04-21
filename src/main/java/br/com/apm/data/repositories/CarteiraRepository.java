package br.com.apm.data.repositories;

import br.com.apm.domain.models.Carteira;
import br.com.apm.domain.models.UserAPI;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CarteiraRepository extends JpaRepository<Carteira, UUID> {

    Carteira findByOwner_Id(UUID userID);
}
