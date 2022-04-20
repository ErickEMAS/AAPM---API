package br.com.apm.core.security;

import br.com.apm.data.repositories.UserRepository;
import br.com.apm.domain.models.UserAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service(value = "userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAPI user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("user não encontrado");
        }

        return user;
    }

}