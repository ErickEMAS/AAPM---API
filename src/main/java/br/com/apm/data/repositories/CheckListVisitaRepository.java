package br.com.apm.data.repositories;

import br.com.apm.domain.models.CheckListVisita;
import br.com.apm.domain.models.QuestionCheckList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CheckListVisitaRepository extends JpaRepository<CheckListVisita, UUID> {
}
