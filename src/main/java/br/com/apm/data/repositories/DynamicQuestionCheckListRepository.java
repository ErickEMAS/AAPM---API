package br.com.apm.data.repositories;

import br.com.apm.domain.models.Carteira;
import br.com.apm.domain.models.DynamicQuestionCheckList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DynamicQuestionCheckListRepository extends JpaRepository<DynamicQuestionCheckList, UUID> {
}
