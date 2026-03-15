package ma.toubkalit.services.organisationService;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.entity.organisation.Employe;
import ma.toubkalit.repositories.organisationRepo.EmployeRepo;
import ma.toubkalit.repositories.organisationRepo.ProfilRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class EmployeServiceImpl implements EmployeService {

    private final EmployeRepo employeRepo;
    private final ProfilRepo profilRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Employe saveEmploye(Employe employe) {
        if (employe.getProfil() != null && employe.getProfil().getId() != null) {
            profilRepo.findById(employe.getProfil().getId())
                    .orElseThrow(() -> new RuntimeException("Profil non trouvé avec l'id : " + employe.getProfil().getId()));
        }

        if (employe.getMatricule() != null && employeRepo.existsByMatricule(employe.getMatricule())) {
            throw new RuntimeException("Un employé avec ce matricule existe déjà");
        }

        if (employe.getLogin() != null && employeRepo.existsByLogin(employe.getLogin())) {
            throw new RuntimeException("Un employé avec ce login existe déjà");
        }

        if (employe.getEmail() != null && employeRepo.existsByEmail(employe.getEmail())) {
            throw new RuntimeException("Un employé avec cet email existe déjà");
        }

        if (employe.getPassword() != null) {
            employe.setPassword(passwordEncoder.encode(employe.getPassword()));
        }

        return employeRepo.save(employe);
    }

    @Override
    public Employe updateEmploye(Integer id, Employe incoming) {
        Employe existing = getEmployeById(id);

        if (incoming.getMatricule() != null && !incoming.getMatricule().trim().isEmpty()) {
            existing.setMatricule(incoming.getMatricule());
        }

        if (incoming.getLogin() != null && !incoming.getLogin().trim().isEmpty()) {
            existing.setLogin(incoming.getLogin());
        }

        if (incoming.getEmail() != null && !incoming.getEmail().trim().isEmpty()) {
            existing.setEmail(incoming.getEmail());
        }

        if (incoming.getNom() != null) existing.setNom(incoming.getNom());
        if (incoming.getPrenom() != null) existing.setPrenom(incoming.getPrenom());
        if (incoming.getTelephone() != null) existing.setTelephone(incoming.getTelephone());
        
        if (incoming.getPassword() != null && !incoming.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(incoming.getPassword()));
        }
        
        if (incoming.getProfil() != null && incoming.getProfil().getId() != null) {
            existing.setProfil(incoming.getProfil());
        }

        return employeRepo.saveAndFlush(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public Employe getEmployeById(Integer id) {
        return employeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employe introuvable avec l'id : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Employe getEmployeByMatricule(String matricule) {
        return employeRepo.findByMatricule(matricule)
                .orElseThrow(() -> new RuntimeException("Employe introuvable avec le matricule : " + matricule));
    }

    @Override
    @Transactional(readOnly = true)
    public Employe getEmployeByLogin(String login) {
        return employeRepo.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Employe introuvable avec le login : " + login));
    }

    @Override
    @Transactional(readOnly = true)
    public Employe getEmployeByEmail(String email) {
        return employeRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employe introuvable avec l'email : " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employe> getAllEmployes() {
        return employeRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employe> getEmployesByProfil(Integer profilId) {
        return employeRepo.findAll().stream()
                .filter(e -> e.getProfil() != null && e.getProfil().getId().equals(profilId))
                .toList();
    }

    @Override
    public void deleteEmploye(Integer id) {
        Employe employe = getEmployeById(id);
        employeRepo.delete(employe);
    }
}