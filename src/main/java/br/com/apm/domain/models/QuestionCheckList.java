package br.com.apm.domain.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
public class QuestionCheckList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String fieldUpdateID;
    private String question;
    private String answer;

    @ElementCollection
    private List<String> alternatives;

    private boolean answerRequired;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "checkListVisita_id")
    private CheckListVisita checkListVisita;

}
