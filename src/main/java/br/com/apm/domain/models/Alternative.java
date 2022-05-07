package br.com.apm.domain.models;

import lombok.Data;
import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
public class Alternative {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private boolean checked;

}
