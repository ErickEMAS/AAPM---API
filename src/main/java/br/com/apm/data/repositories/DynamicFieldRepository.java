package br.com.apm.data.repositories;

import br.com.apm.domain.models.DynamicField;
import br.com.apm.domain.models.UserAPI;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DynamicFieldRepository extends JpaRepository<DynamicField, UUID> {
}
