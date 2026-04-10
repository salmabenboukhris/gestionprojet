package ma.toubkalit.suiviprojet.repositories;

import ma.toubkalit.suiviprojet.entities.Phase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PhaseRepository extends JpaRepository<Phase, Long> {

    List<Phase> findByProjetId(Long projetId);

    // ── Reporting queries with JOIN FETCH to avoid N+1 lazy loading ──────────

    @Query("SELECT p FROM Phase p JOIN FETCH p.projet proj JOIN FETCH proj.chefProjet JOIN FETCH proj.organisme WHERE p.etatRealisation = true AND p.etatFacturation = false")
    List<Phase> findByEtatRealisationTrueAndEtatFacturationFalse();

    @Query("SELECT p FROM Phase p JOIN FETCH p.projet proj JOIN FETCH proj.chefProjet JOIN FETCH proj.organisme WHERE p.etatFacturation = true AND p.etatPaiement = false")
    List<Phase> findByEtatFacturationTrueAndEtatPaiementFalse();

    @Query("SELECT p FROM Phase p JOIN FETCH p.projet proj JOIN FETCH proj.chefProjet JOIN FETCH proj.organisme WHERE p.etatPaiement = true")
    List<Phase> findByEtatPaiementTrue();

    @Query("SELECT p FROM Phase p JOIN FETCH p.projet proj JOIN FETCH proj.chefProjet JOIN FETCH proj.organisme")
    List<Phase> findAllWithProjetAndChef();

    List<Phase> findByDateDebutBetween(LocalDate dateDebut, LocalDate dateFin);

    List<Phase> findByProjetChefProjetId(Long chefProjetId);
}