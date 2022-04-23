package br.com.apm.data.repositories;

import br.com.apm.domain.models.Alternative;
import br.com.apm.domain.models.QuestionCheckList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AlternativeRepository extends JpaRepository<Alternative, UUID> {
}
