package ma.toubkalit.suiviprojet.repositories;

import ma.toubkalit.suiviprojet.entities.Phase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PhaseRepository extends JpaRepository<Phase, Long> {

    List<Phase> findByProjetId(Long projetId);

    List<Phase> findByEtatRealisationTrueAndEtatFacturationFalse();

    List<Phase> findByEtatFacturationTrueAndEtatPaiementFalse();

    List<Phase> findByEtatPaiementTrue();

    List<Phase> findByDateDebutBetween(LocalDate dateDebut, LocalDate dateFin);

    List<Phase> findByProjetChefProjetId(Long chefProjetId);
}