package ma.toubkalit.services.projetService;

import ma.toubkalit.entity.projet.Affectation;
import ma.toubkalit.entity.projet.AffectationId;

import java.util.List;

public interface AffectationService {
    Affectation saveAffectation(Affectation affectation);
    void deleteAffectation(AffectationId id);
    List<Affectation> getAffectationsByPhase(Integer phaseId);
    List<Affectation> getAffectationsByEmploye(Integer employeId);
    Affectation getAffectationById(AffectationId id);
    Affectation updateAffectation(AffectationId id, Affectation affectation);
}
