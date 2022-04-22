package br.com.apm.domain.models;

import br.com.apm.domain.enums.TypeField;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
public class DynamicField {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private TypeField type;

    @OneToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    private DynamicQuestionCheckList DanymicquestionCheckList;
}
