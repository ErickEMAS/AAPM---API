package br.com.apm.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class QuestionCheckList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID fieldUpdateID;
    private String question;
    private String answer;

    private boolean answerRequired;

    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name = "questionCheckList_alternatives", joinColumns = @JoinColumn(name = "questionCheckList_id"))
    @Fetch(FetchMode.SUBSELECT)
    private List<String> alternatives;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "checkListVisita_id")
    private CheckListVisita checkListVisita;

}
