package ma.toubkalit.services.projetService;

import ma.toubkalit.entity.projet.Phase;
import ma.toubkalit.enums.EtatFacturation;
import ma.toubkalit.enums.EtatPaiement;

import java.util.List;

public interface PhaseService {

    Phase savePhase(Phase phase);

    Phase updatePhase(Integer id, Phase phase);

    Phase getPhaseById(Integer id);

    List<Phase> getAllPhases();

    List<Phase> getPhasesByEtatFacturation(EtatFacturation etatFacturation);

    List<Phase> getPhasesByEtatPaiement(EtatPaiement etatPaiement);

    void deletePhase(Integer id);
}