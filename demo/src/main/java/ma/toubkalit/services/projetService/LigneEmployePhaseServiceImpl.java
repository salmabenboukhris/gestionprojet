package ma.toubkalit.services.projetService;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.entity.projet.LigneEmployePhase;
import ma.toubkalit.entity.projet.LigneEmployePhaseId;
import ma.toubkalit.repositories.projetRepo.LigneEmployePhaseRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LigneEmployePhaseServiceImpl implements LigneEmployePhaseService {

    private final LigneEmployePhaseRepo ligneEmployePhaseRepo;

    @Override
    public LigneEmployePhase saveLigneEmployePhase(LigneEmployePhase ligneEmployePhase) {
        Integer employeId = ligneEmployePhase.getEmploye().getId();
        Integer phaseId = ligneEmployePhase.getPhase().getId();

        LigneEmployePhaseId id = new LigneEmployePhaseId(employeId, phaseId);
        ligneEmployePhase.setId(id);

        if (ligneEmployePhase.getDateFin().isBefore(ligneEmployePhase.getDateDebut())) {
            throw new RuntimeException("La date de fin doit etre superieure ou egale a la date de debut.");
        }

        if (ligneEmployePhaseRepo.existsById(id)) {
            throw new RuntimeException("Cette affectation existe deja.");
        }

        return ligneEmployePhaseRepo.save(ligneEmployePhase);
    }

    @Override
    public LigneEmployePhase updateLigneEmployePhase(Integer employeId, Integer phaseId, LigneEmployePhase ligneEmployePhase) {
        LigneEmployePhase existing = getLigneEmployePhaseById(employeId, phaseId);

        if (ligneEmployePhase.getDateFin().isBefore(ligneEmployePhase.getDateDebut())) {
            throw new RuntimeException("La date de fin doit etre superieure ou egale a la date de debut.");
        }

        existing.setDateDebut(ligneEmployePhase.getDateDebut());
        existing.setDateFin(ligneEmployePhase.getDateFin());

        return ligneEmployePhaseRepo.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public LigneEmployePhase getLigneEmployePhaseById(Integer employeId, Integer phaseId) {
        LigneEmployePhaseId id = new LigneEmployePhaseId(employeId, phaseId);

        return ligneEmployePhaseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("LigneEmployePhase introuvable."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LigneEmployePhase> getAllLignesEmployePhase() {
        return ligneEmployePhaseRepo.findAll();
    }

    @Override
    public void deleteLigneEmployePhase(Integer employeId, Integer phaseId) {
        LigneEmployePhase ligneEmployePhase = getLigneEmployePhaseById(employeId, phaseId);
        ligneEmployePhaseRepo.delete(ligneEmployePhase);
    }
}