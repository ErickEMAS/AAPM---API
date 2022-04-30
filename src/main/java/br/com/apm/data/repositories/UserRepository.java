package br.com.apm.data.repositories;

import br.com.apm.domain.models.UserAPI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserAPI, UUID> {

    UserAPI findByEmail(String email);
    UserAPI findByCpf(String cpf);

    List<UserAPI> findByRoles_Name(String roleName);

}
