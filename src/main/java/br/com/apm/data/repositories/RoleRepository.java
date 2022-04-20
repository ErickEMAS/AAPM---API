package br.com.apm.data.repositories;

import br.com.apm.domain.models.Role;
import org.hibernate.id.GUIDGenerator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Role findByName(String name);

}
