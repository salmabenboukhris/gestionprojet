package ma.toubkalit.suiviprojet.repositories;

import ma.toubkalit.suiviprojet.entities.Employe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeRepository extends JpaRepository<Employe, Long> {

    Optional<Employe> findByMatricule(String matricule);

    Optional<Employe> findByLogin(String login);

    Optional<Employe> findByEmail(String email);

    boolean existsByMatricule(String matricule);

    boolean existsByLogin(String login);

    boolean existsByEmail(String email);

    List<Employe> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);
}