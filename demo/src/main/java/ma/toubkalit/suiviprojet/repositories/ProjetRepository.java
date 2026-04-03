package ma.toubkalit.suiviprojet.repositories;

import ma.toubkalit.suiviprojet.entities.Projet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProjetRepository extends JpaRepository<Projet, Long> {

    Optional<Projet> findByCode(String code);

    boolean existsByCode(String code);

    List<Projet> findByNomContainingIgnoreCase(String nom);

    List<Projet> findByOrganismeId(Long organismeId);

    List<Projet> findByChefProjetId(Long chefProjetId);

    boolean existsByOrganismeId(Long organismeId);

    List<Projet> findByDateFinBefore(LocalDate date);

    List<Projet> findByDateFinGreaterThanEqual(LocalDate date);
}