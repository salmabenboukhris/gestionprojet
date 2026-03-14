package ma.toubkalit.services.organisationService;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.entity.organisation.Employe;
import ma.toubkalit.repositories.organisationRepo.EmployeRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeServiceImpl implements EmployeService {

    private final EmployeRepo employeRepo;

    @Override
    public Employe saveEmploye(Employe employe) {
        if (employeRepo.findByMatricule(employe.getMatricule()).isPresent()) {
            throw new RuntimeException("Un employe avec ce matricule existe deja.");
        }
        if (employeRepo.findByLogin(employe.getLogin()).isPresent()) {
            throw new RuntimeException("Un employe avec ce login existe deja.");
        }
        if (employeRepo.findByEmail(employe.getEmail()).isPresent()) {
            throw new RuntimeException("Un employe avec cet email existe deja.");
        }

        return employeRepo.save(employe);
    }

    @Override
    public Employe updateEmploye(Integer id, Employe employe) {
        Employe existing = getEmployeById(id);

        employeRepo.findByMatricule(employe.getMatricule())
                .ifPresent(e -> {
                    if (e.getId() != id) {
                        throw new RuntimeException("Un employe avec ce matricule existe deja.");
                    }
                });

        employeRepo.findByLogin(employe.getLogin())
                .ifPresent(e -> {
                    if (e.getId() != id) {
                        throw new RuntimeException("Un employe avec ce login existe deja.");
                    }
                });

        employeRepo.findByEmail(employe.getEmail())
                .ifPresent(e -> {
                    if (e.getId() != id) {
                        throw new RuntimeException("Un employe avec cet email existe deja.");
                    }
                });

        existing.setMatricule(employe.getMatricule());
        existing.setNom(employe.getNom());
        existing.setPrenom(employe.getPrenom());
        existing.setTelephone(employe.getTelephone());
        existing.setEmail(employe.getEmail());
        existing.setLogin(employe.getLogin());
        existing.setPassword(employe.getPassword());
        existing.setProfil(employe.getProfil());

        return employeRepo.save(existing);
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
    public void deleteEmploye(Integer id) {
        Employe employe = getEmployeById(id);
        employeRepo.delete(employe);
    }
}