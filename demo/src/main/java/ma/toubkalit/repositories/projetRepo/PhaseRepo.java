package ma.toubkalit.repositories.projetRepo;

import ma.toubkalit.entity.projet.Phase;
import ma.toubkalit.enums.EtatFacturation;
import ma.toubkalit.enums.EtatPaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhaseRepo extends JpaRepository<Phase, Integer> {

    List<Phase> findByEtatFacturation(EtatFacturation etatFacturation);

    List<Phase> findByEtatPaiement(EtatPaiement etatPaiement);

    List<Phase> findByProjetId(Integer projetId);

}