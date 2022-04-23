package br.com.apm.data.repositories;

import br.com.apm.domain.models.DynamicQuestionCheckList;
import br.com.apm.domain.models.QuestionCheckList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface QuestionCheckListRepository extends JpaRepository<QuestionCheckList, UUID> {

    List<QuestionCheckList> findByCheckListVisita_id(UUID checkListVisitaId);

}
