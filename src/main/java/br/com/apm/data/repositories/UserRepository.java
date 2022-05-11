package br.com.apm.data.repositories;

import br.com.apm.domain.models.DynamicQuestionCheckList;
import br.com.apm.domain.models.UserAPI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserAPI, UUID> {

    UserAPI findByEmail(String email);
    UserAPI findByCpf(String cpf);

    Page<UserAPI> findByRoles_NameAndActiveAndFullNameContainingIgnoreCase(String roleName, boolean active, String fullName, Pageable pageable);

    Page<UserAPI> findByActiveAndFullNameContainingIgnoreCase(boolean active, String fullName, Pageable pageable);
    Page<UserAPI> findByActiveAndRoles_NameAndFullNameContainingIgnoreCase(boolean active, String fullName, String roleName, Pageable pageable);

}
