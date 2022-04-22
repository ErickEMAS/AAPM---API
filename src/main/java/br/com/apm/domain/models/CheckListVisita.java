package br.com.apm.domain.models;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class CheckListVisita {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private LocalDateTime dataVisita;

    @OneToMany(mappedBy="checkListVisita")
    private List<QuestionCheckList> questions;

}
