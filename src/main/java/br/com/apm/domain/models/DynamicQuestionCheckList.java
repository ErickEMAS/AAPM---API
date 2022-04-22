package br.com.apm.domain.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class DynamicQuestionCheckList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String question;

    @ElementCollection
    private List<String> alternatives;

    private boolean answerRequired;
    private boolean active;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fieldUpdate_id", referencedColumnName = "id")
    private DynamicField fieldUpdate;

}
