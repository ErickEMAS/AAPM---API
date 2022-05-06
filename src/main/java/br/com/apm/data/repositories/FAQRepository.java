package br.com.apm.data.repositories;

import br.com.apm.domain.models.FAQ;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FAQRepository extends JpaRepository<FAQ, UUID> {

    Page<FAQ> findByQuestionContainingIgnoreCaseOrAnswerContainingIgnoreCase(String question, String answer, Pageable pageable);

}
