package br.com.apm.domain.models;

import lombok.Data;
import org.hibernate.id.GUIDGenerator;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@Table(name= "Role")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @Override
    public String getAuthority() {
        return name;
    }

}
