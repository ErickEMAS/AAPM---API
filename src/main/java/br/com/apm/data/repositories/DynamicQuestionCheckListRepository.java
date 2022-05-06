package br.com.apm.data.repositories;

import br.com.apm.domain.models.Carteira;
import br.com.apm.domain.models.DynamicQuestionCheckList;
import br.com.apm.domain.models.UserAPI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DynamicQuestionCheckListRepository extends JpaRepository<DynamicQuestionCheckList, UUID> {

    List<DynamicQuestionCheckList> findByActiveTrue();
    Page<DynamicQuestionCheckList> findByActiveTrue(Pageable pageable);
    Page<DynamicQuestionCheckList> findByActiveFalse(Pageable pageable);
    Page<DynamicQuestionCheckList> findAll(Pageable pageable);

}
