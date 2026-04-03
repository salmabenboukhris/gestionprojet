package ma.toubkalit.suiviprojet.security;

import ma.toubkalit.suiviprojet.entities.Employe;
import ma.toubkalit.suiviprojet.repositories.EmployeRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeRepository employeRepository;

    public CustomUserDetailsService(EmployeRepository employeRepository) {
        this.employeRepository = employeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Employe emp = employeRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        return new User(
                emp.getLogin(),
                emp.getPassword(),
                Collections.emptyList()
        );
    }
}