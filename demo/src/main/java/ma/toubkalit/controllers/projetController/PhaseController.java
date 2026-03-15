package ma.toubkalit.controllers.projetController;


import lombok.RequiredArgsConstructor;
import ma.toubkalit.entity.projet.Phase;
import ma.toubkalit.entity.projet.Projet;
import ma.toubkalit.enums.EtatFacturation;
import ma.toubkalit.enums.EtatPaiement;
import ma.toubkalit.enums.EtatRealisation;
import ma.toubkalit.repositories.projetRepo.PhaseRepo;
import ma.toubkalit.repositories.projetRepo.ProjetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PhaseController {

    private final PhaseRepo phaseRepo;
    private final ProjetRepository projetRepo;

    @PostMapping("/projets/{projetId}/phases")
    public ResponseEntity<Phase> create(@PathVariable Integer projetId, @RequestBody Phase phase) {
        return projetRepo.findById(projetId)
                .map(projet -> {
                    // Vérifier que les dates sont dans l'intervalle du projet
                    if (phase.getDateDebut().isBefore(projet.getDateDebut()) ||
                            phase.getDateFin().isAfter(projet.getDateFin())) {
                        throw new RuntimeException("Les dates de la phase doivent être comprises entre " +
                                projet.getDateDebut() + " et " + projet.getDateFin());
                    }

                    // Vérifier que le montant total des phases ne dépasse pas le montant du projet
                    double totalMontantPhases = projet.getPhases().stream()
                            .mapToDouble(Phase::getMontant)
                            .sum() + phase.getMontant();

                    if (totalMontantPhases > projet.getMontant()) {
                        throw new RuntimeException("Le montant total des phases (" + totalMontantPhases +
                                ") dépasse le montant du projet (" + projet.getMontant() + ")");
                    }

                    phase.setProjet(projet);

                    // Initialiser les états par défaut si non fournis
                    if (phase.getEtatRealisation() == null) {
                        phase.setEtatRealisation(EtatRealisation.NON_TERMINEE);
                    }
                    if (phase.getEtatFacturation() == null) {
                        phase.setEtatFacturation(EtatFacturation.NON_FACTUREE);
                    }
                    if (phase.getEtatPaiement() == null) {
                        phase.setEtatPaiement(EtatPaiement.NON_PAYEE);
                    }

                    Phase savedPhase = phaseRepo.save(phase);
                    return new ResponseEntity<>(savedPhase, HttpStatus.CREATED);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/phases/{id}")
    public ResponseEntity<Phase> getById(@PathVariable Integer id) {
        return phaseRepo.findById(id)
                .map(phase -> ResponseEntity.ok(phase))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/projets/{projetId}/phases")
    public ResponseEntity<List<Phase>> getByProjetId(@PathVariable Integer projetId) {
        return projetRepo.findById(projetId)
                .map(projet -> {
                    List<Phase> phases = phaseRepo.findAll().stream()
                            .filter(p -> p.getProjet() != null &&
                                    p.getProjet().getId() == projetId)
                            .toList();
                    return ResponseEntity.ok(phases);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/phases")
    public List<Phase> getAll() {
        return phaseRepo.findAll();
    }

    @GetMapping("/phases/etat-facturation/{etat}")
    public List<Phase> getByEtatFacturation(@PathVariable EtatFacturation etat) {
        return phaseRepo.findByEtatFacturation(etat);
    }

    @GetMapping("/phases/etat-paiement/{etat}")
    public List<Phase> getByEtatPaiement(@PathVariable EtatPaiement etat) {
        return phaseRepo.findByEtatPaiement(etat);
    }

    @GetMapping("/phases/etat-realisation/{etat}")
    public List<Phase> getByEtatRealisation(@PathVariable EtatRealisation etat) {
        return phaseRepo.findAll().stream()
                .filter(p -> p.getEtatRealisation() == etat)
                .toList();
    }

    @GetMapping("/phases/non-facturees")
    public List<Phase> getPhasesNonFacturees() {
        return phaseRepo.findByEtatFacturation(EtatFacturation.NON_FACTUREE);
    }

    @GetMapping("/phases/facturees-non-payees")
    public List<Phase> getPhasesFactureesNonPayees() {
        return phaseRepo.findAll().stream()
                .filter(p -> p.getEtatFacturation() == EtatFacturation.FACTUREE &&
                        p.getEtatPaiement() == EtatPaiement.NON_PAYEE)
                .toList();
    }

    @PutMapping("/phases/{id}")
    public ResponseEntity<Phase> update(@PathVariable Integer id, @RequestBody Phase phase) {
        return phaseRepo.findById(id)
                .map(existingPhase -> {
                    // Vérifier que les dates sont dans l'intervalle du projet
                    Projet projet = existingPhase.getProjet();
                    if (projet != null) {
                        if (phase.getDateDebut().isBefore(projet.getDateDebut()) ||
                                phase.getDateFin().isAfter(projet.getDateFin())) {
                            throw new RuntimeException("Les dates de la phase doivent être comprises entre " +
                                    projet.getDateDebut() + " et " + projet.getDateFin());
                        }
                    }

                    existingPhase.setCode(phase.getCode());
                    existingPhase.setLibelle(phase.getLibelle());
                    existingPhase.setDescription(phase.getDescription());
                    existingPhase.setDateDebut(phase.getDateDebut());
                    existingPhase.setDateFin(phase.getDateFin());
                    existingPhase.setMontant(phase.getMontant());


                    Phase updatedPhase = phaseRepo.save(existingPhase);
                    return ResponseEntity.ok(updatedPhase);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/phases/{id}/realisation")
    public ResponseEntity<Phase> updateEtatRealisation(
            @PathVariable Integer id,
            @RequestParam EtatRealisation etat) {
        return phaseRepo.findById(id)
                .map(phase -> {
                    phase.setEtatRealisation(etat);
                    Phase updatedPhase = phaseRepo.save(phase);
                    return ResponseEntity.ok(updatedPhase);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/phases/{id}/facturation")
    public ResponseEntity<Phase> updateEtatFacturation(
            @PathVariable Integer id,
            @RequestParam EtatFacturation etat) {
        return phaseRepo.findById(id)
                .map(phase -> {
                    phase.setEtatFacturation(etat);
                    Phase updatedPhase = phaseRepo.save(phase);
                    return ResponseEntity.ok(updatedPhase);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/phases/{id}/paiement")
    public ResponseEntity<Phase> updateEtatPaiement(
            @PathVariable Integer id,
            @RequestParam EtatPaiement etat) {
        return phaseRepo.findById(id)
                .map(phase -> {
                    phase.setEtatPaiement(etat);
                    Phase updatedPhase = phaseRepo.save(phase);
                    return ResponseEntity.ok(updatedPhase);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/phases/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (phaseRepo.existsById(id)) {
            phaseRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
