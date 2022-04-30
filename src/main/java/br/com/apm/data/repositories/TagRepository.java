package br.com.apm.data.repositories;

import br.com.apm.domain.models.Seller;
import br.com.apm.domain.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, UUID> {
}
