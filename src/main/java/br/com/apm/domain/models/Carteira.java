package br.com.apm.domain.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
public class Carteira {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Seller> sellers;

    @OneToOne(mappedBy = "userapi", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, optional = false)
    private UserAPI owner;

}
