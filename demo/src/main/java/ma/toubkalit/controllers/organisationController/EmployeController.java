package ma.toubkalit.controllers.organisationController;


import lombok.RequiredArgsConstructor;
import ma.toubkalit.entity.organisation.Employe;
import ma.toubkalit.repositories.organisationRepo.EmployeRepo;
import ma.toubkalit.repositories.organisationRepo.ProfilRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeController {

    private final EmployeRepo employeRepo;
    private final ProfilRepo profilRepo;

    @PostMapping
    public ResponseEntity<Employe> create(@RequestBody Employe employe) {
        // Vérifier que le profil existe
        if (employe.getProfil() != null && employe.getProfil().getId() != 0) {
            profilRepo.findById(employe.getProfil().getId())
                    .orElseThrow(() -> new RuntimeException("Profil non trouvé avec l'id : " + employe.getProfil().getId()));
        }

        // Vérifier l'unicité du matricule
        if (employe.getMatricule() != null) {
            employeRepo.findByMatricule(employe.getMatricule())
                    .ifPresent(e -> {
                        throw new RuntimeException("Un employé avec ce matricule existe déjà");
                    });
        }

        // Vérifier l'unicité du login
        if (employe.getLogin() != null) {
            employeRepo.findByLogin(employe.getLogin())
                    .ifPresent(e -> {
                        throw new RuntimeException("Un employé avec ce login existe déjà");
                    });
        }

        // Vérifier l'unicité de l'email
        if (employe.getEmail() != null) {
            employeRepo.findByEmail(employe.getEmail())
                    .ifPresent(e -> {
                        throw new RuntimeException("Un employé avec cet email existe déjà");
                    });
        }

        Employe savedEmploye = employeRepo.save(employe);
        return new ResponseEntity<>(savedEmploye, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employe> getById(@PathVariable Integer id) {
        return employeRepo.findById(id)
                .map(employe -> ResponseEntity.ok(employe))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/matricule/{matricule}")
    public ResponseEntity<Employe> getByMatricule(@PathVariable String matricule) {
        return employeRepo.findByMatricule(matricule)
                .map(employe -> ResponseEntity.ok(employe))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/login/{login}")
    public ResponseEntity<Employe> getByLogin(@PathVariable String login) {
        return employeRepo.findByLogin(login)
                .map(employe -> ResponseEntity.ok(employe))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Employe> getByEmail(@PathVariable String email) {
        return employeRepo.findByEmail(email)
                .map(employe -> ResponseEntity.ok(employe))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Employe> getAll() {
        return employeRepo.findAll();
    }

    @GetMapping("/profil/{profilId}")
    public List<Employe> getByProfilId(@PathVariable Integer profilId) {
        return employeRepo.findAll().stream()
                .filter(employe -> employe.getProfil() != null &&
                        employe.getProfil().getId() == profilId)
                .toList();
    }

    @GetMapping("/disponibles")
    public List<Employe> getDisponibles(@RequestParam(required = false) String dateDebut,
                                        @RequestParam(required = false) String dateFin) {

        return employeRepo.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employe> update(@PathVariable Integer id, @RequestBody Employe employe) {
        return employeRepo.findById(id)
                .map(existingEmploye -> {
                    if (employe.getMatricule() != null &&
                            !employe.getMatricule().equals(existingEmploye.getMatricule())) {
                        employeRepo.findByMatricule(employe.getMatricule())
                                .ifPresent(e -> {
                                    throw new RuntimeException("Un employé avec ce matricule existe déjà");
                                });
                        existingEmploye.setMatricule(employe.getMatricule());
                    }

                    // Vérifier l'unicité du login si modifié
                    if (employe.getLogin() != null &&
                            !employe.getLogin().equals(existingEmploye.getLogin())) {
                        employeRepo.findByLogin(employe.getLogin())
                                .ifPresent(e -> {
                                    throw new RuntimeException("Un employé avec ce login existe déjà");
                                });
                        existingEmploye.setLogin(employe.getLogin());
                    }

                    // Vérifier l'unicité de l'email si modifié
                    if (employe.getEmail() != null &&
                            !employe.getEmail().equals(existingEmploye.getEmail())) {
                        employeRepo.findByEmail(employe.getEmail())
                                .ifPresent(e -> {
                                    throw new RuntimeException("Un employé avec cet email existe déjà");
                                });
                        existingEmploye.setEmail(employe.getEmail());
                    }

                    // Mettre à jour les autres champs
                    if (employe.getNom() != null) existingEmploye.setNom(employe.getNom());
                    if (employe.getPrenom() != null) existingEmploye.setPrenom(employe.getPrenom());
                    if (employe.getTelephone() != null) existingEmploye.setTelephone(employe.getTelephone());
                    if (employe.getPassword() != null) existingEmploye.setPassword(employe.getPassword());

                    // Mettre à jour le profil si fourni
                    if (employe.getProfil() != null && employe.getProfil().getId() != 0) {
                        profilRepo.findById(employe.getProfil().getId())
                                .ifPresentOrElse(
                                        existingEmploye::setProfil,
                                        () -> { throw new RuntimeException("Profil non trouvé"); }
                                );
                    }

                    Employe updatedEmploye = employeRepo.save(existingEmploye);
                    return ResponseEntity.ok(updatedEmploye);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (employeRepo.existsById(id)) {
            employeRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}