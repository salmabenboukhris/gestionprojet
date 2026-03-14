package ma.toubkalit.services.projetService;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.entity.projet.Phase;
import ma.toubkalit.entity.projet.Projet;
import ma.toubkalit.enums.EtatFacturation;
import ma.toubkalit.enums.EtatPaiement;
import ma.toubkalit.repositories.projetRepo.PhaseRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PhaseServiceImpl implements PhaseService {

    private final PhaseRepo phaseRepo;

    @Override
    public Phase savePhase(Phase phase) {
        if (phase.getDateFin().isBefore(phase.getDateDebut())) {
            throw new RuntimeException("La date de fin de la phase doit etre superieure ou egale a la date de debut.");
        }

        Projet projet = phase.getProjet();
        if (projet != null) {
            if (phase.getDateDebut().isBefore(projet.getDateDebut()) || phase.getDateFin().isAfter(projet.getDateFin())) {
                throw new RuntimeException("Les dates de la phase doivent etre incluses dans celles du projet.");
            }
        }

        return phaseRepo.save(phase);
    }

    @Override
    public Phase updatePhase(Integer id, Phase phase) {
        Phase existing = getPhaseById(id);

        if (phase.getDateFin().isBefore(phase.getDateDebut())) {
            throw new RuntimeException("La date de fin de la phase doit etre superieure ou egale a la date de debut.");
        }

        Projet projet = phase.getProjet();
        if (projet != null) {
            if (phase.getDateDebut().isBefore(projet.getDateDebut()) || phase.getDateFin().isAfter(projet.getDateFin())) {
                throw new RuntimeException("Les dates de la phase doivent etre incluses dans celles du projet.");
            }
        }

        existing.setCode(phase.getCode());
        existing.setLibelle(phase.getLibelle());
        existing.setDescription(phase.getDescription());
        existing.setDateDebut(phase.getDateDebut());
        existing.setDateFin(phase.getDateFin());
        existing.setMontant(phase.getMontant());
        existing.setEtatRealisation(phase.getEtatRealisation());
        existing.setEtatFacturation(phase.getEtatFacturation());
        existing.setEtatPaiement(phase.getEtatPaiement());
        existing.setProjet(phase.getProjet());

        return phaseRepo.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public Phase getPhaseById(Integer id) {
        return phaseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Phase introuvable avec l'id : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Phase> getAllPhases() {
        return phaseRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Phase> getPhasesByEtatFacturation(EtatFacturation etatFacturation) {
        return phaseRepo.findByEtatFacturation(etatFacturation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Phase> getPhasesByEtatPaiement(EtatPaiement etatPaiement) {
        return phaseRepo.findByEtatPaiement(etatPaiement);
    }

    @Override
    public void deletePhase(Integer id) {
        Phase phase = getPhaseById(id);
        phaseRepo.delete(phase);
    }
}