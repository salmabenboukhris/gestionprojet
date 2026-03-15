package ma.toubkalit.services.projetService;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.entity.projet.Affectation;
import ma.toubkalit.entity.projet.AffectationId;
import ma.toubkalit.repositories.projetRepo.AffectationRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AffectationServiceImpl implements AffectationService {

    private final AffectationRepo affectationRepo;

    @Override
    public Affectation saveAffectation(Affectation affectation) {
        // Règles métier à ajouter plus tard (Phase 8)
        return affectationRepo.save(affectation);
    }

    @Override
    public void deleteAffectation(AffectationId id) {
        affectationRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Affectation> getAffectationsByPhase(Integer phaseId) {
        return affectationRepo.findByPhaseId(phaseId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Affectation> getAffectationsByEmploye(Integer employeId) {
        return affectationRepo.findByEmployeId(employeId);
    }

    @Override
    @Transactional(readOnly = true)
    public Affectation getAffectationById(AffectationId id) {
        return affectationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Affectation introuvable."));
    }

    @Override
    public Affectation updateAffectation(AffectationId id, Affectation affectation) {
        Affectation existing = getAffectationById(id);
        existing.setDateDebut(affectation.getDateDebut());
        existing.setDateFin(affectation.getDateFin());
        return affectationRepo.save(existing);
    }
}
