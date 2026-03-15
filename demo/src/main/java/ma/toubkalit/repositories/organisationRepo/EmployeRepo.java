package ma.toubkalit.repositories.organisationRepo;

import ma.toubkalit.entity.organisation.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeRepo extends JpaRepository<Employe, Integer> {

    Optional<Employe> findByMatricule(String matricule);

    Optional<Employe> findByLogin(String login);

    Optional<Employe> findByEmail(String email);

    boolean existsByMatricule(String matricule);

    boolean existsByLogin(String login);

    boolean existsByEmail(String email);

}