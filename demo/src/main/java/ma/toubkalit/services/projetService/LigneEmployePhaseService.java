package ma.toubkalit.services.projetService;

import ma.toubkalit.entity.projet.LigneEmployePhase;

import java.util.List;

public interface LigneEmployePhaseService {

    LigneEmployePhase saveLigneEmployePhase(LigneEmployePhase ligneEmployePhase);

    LigneEmployePhase updateLigneEmployePhase(Integer employeId, Integer phaseId, LigneEmployePhase ligneEmployePhase);

    LigneEmployePhase getLigneEmployePhaseById(Integer employeId, Integer phaseId);

    List<LigneEmployePhase> getAllLignesEmployePhase();

    void deleteLigneEmployePhase(Integer employeId, Integer phaseId);
}