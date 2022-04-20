package br.com.apm.data.repositories;

import br.com.apm.domain.models.UserAPI;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserAPI, UUID> {

    UserAPI findByEmail(String email);
    UserAPI findByCpf(String cpf);

}
