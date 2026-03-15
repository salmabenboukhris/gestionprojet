package ma.toubkalit.config.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.toubkalit.entity.organisation.Employe;
import ma.toubkalit.repositories.organisationRepo.EmployeRepo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final EmployeRepo employeRepo;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        log.info("Chargement des détails pour l'utilisateur: {}", login);
        Employe employe = employeRepo.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec le login: " + login));

        String role = (employe.getProfil() != null && employe.getProfil().getRoleCode() != null)
                ? employe.getProfil().getRoleCode().name()
                : "USER";

        log.info("Rôle détecté pour {}: {}", login, role);
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
        log.info("Autorités assignées: {}", authorities);

        return new User(employe.getLogin(), employe.getPassword(), authorities);
    }
}
