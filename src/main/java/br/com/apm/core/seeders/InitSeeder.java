package br.com.apm.core.seeders;

import br.com.apm.data.repositories.RoleRepository;
import br.com.apm.data.repositories.UserRepository;
import br.com.apm.domain.models.Role;
import br.com.apm.domain.models.UserAPI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class InitSeeder {
    private static RoleRepository roleRepository;
    private static UserRepository userRepository;

    private UserAPI userAdmin;
    private Role roleAdmin;
    private Role roleUser;

    @Autowired
    public InitSeeder(
            RoleRepository _roleRepository,
            UserRepository _userRepository
    ) {
        this.roleRepository = _roleRepository;
        this.userRepository = _userRepository;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        if (roleRepository.findAll().size() == 0) {
            seedAppConfig();
            seedUserTable();
        }
    }

    private void seedAppConfig() {
        this.roleAdmin = new Role();
        this.roleAdmin.setName("ROLE_ADMIN");
        this.roleAdmin = roleRepository.save(roleAdmin);

        this.roleUser = new Role();
        this.roleUser.setName("ROLE_USER");
        this.roleUser = roleRepository.save(roleUser);
    }

    private void seedUserTable() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        UserAPI user = new UserAPI();
        user.setEmail("admin@apm.com.br");
        user.setPassword(bCryptPasswordEncoder.encode("lumos123456"));
        user.setFullName("Admin");
        user.setCpf("00000000000");
        user.setEmailConfirmed(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        List<Role> listRoles = new ArrayList<>();
        listRoles.add(roleAdmin);
        user.setRoles(listRoles);

        userRepository.save(user);

        user = new UserAPI();
        user.setEmail("user@apm.com.br");
        user.setPassword(bCryptPasswordEncoder.encode("lumos123456"));
        user.setFullName("User");
        user.setCpf("11111111111");
        user.setEmailConfirmed(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        listRoles = new ArrayList<>();
        listRoles.add(roleUser);
        user.setRoles(listRoles);

        userRepository.save(user);
    }

}


