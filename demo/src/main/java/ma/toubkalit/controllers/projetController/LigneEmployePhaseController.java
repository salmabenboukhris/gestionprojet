package ma.toubkalit.controllers.projetController;


import lombok.RequiredArgsConstructor;
import ma.toubkalit.entity.projet.LigneEmployePhase;
import ma.toubkalit.repositories.organisationRepo.EmployeRepo;
import ma.toubkalit.repositories.projetRepo.LigneEmployePhaseRepo;
import ma.toubkalit.repositories.projetRepo.PhaseRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LigneEmployePhaseController {

    private final LigneEmployePhaseRepo ligneEmployePhaseRepo;
    private final PhaseRepo phaseRepo;
    private final EmployeRepo employeRepo;

    @PostMapping("/phases/{phaseId}/employees/{employeId}")
    public ResponseEntity<LigneEmployePhase> affecter(
            @PathVariable Integer phaseId,
            @PathVariable Integer employeId,
            @RequestBody LigneEmployePhase affectation) {

        return phaseRepo.findById(phaseId)
                .flatMap(phase -> employeRepo.findById(employeId)
                        .map(employe -> {
                            boolean existeDeja = ligneEmployePhaseRepo.findAll().stream()
                                    .anyMatch(a -> a.getEmploye() != null &&
                                            a.getPhase() != null &&
                                            a.getEmploye().getId() == employeId &&
                                            a.getPhase().getId() == phaseId);

                            if (existeDeja) {
                                throw new RuntimeException("Cette affectation existe déjà");
                            }

                            // Vérifier que les dates sont dans l'intervalle de la phase
                            if (affectation.getDateDebut().isBefore(phase.getDateDebut()) ||
                                    affectation.getDateFin().isAfter(phase.getDateFin())) {
                                throw new RuntimeException("Les dates d'affectation doivent être comprises dans les dates de la phase");
                            }

                            affectation.setEmploye(employe);
                            affectation.setPhase(phase);

                            LigneEmployePhase savedAffectation = ligneEmployePhaseRepo.save(affectation);
                            return new ResponseEntity<>(savedAffectation, HttpStatus.CREATED);
                        }))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/phases/{phaseId}/employees")
    public ResponseEntity<List<LigneEmployePhase>> getByPhaseId(@PathVariable Integer phaseId) {
        return phaseRepo.findById(phaseId)
                .map(phase -> {
                    List<LigneEmployePhase> affectations = ligneEmployePhaseRepo.findAll().stream()
                            .filter(a -> a.getPhase() != null &&
                                    a.getPhase().getId() == phaseId)
                            .toList();
                    return ResponseEntity.ok(affectations);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/employees/{employeId}/phases")
    public ResponseEntity<List<LigneEmployePhase>> getByEmployeId(@PathVariable Integer employeId) {
        return employeRepo.findById(employeId)
                .map(employe -> {
                    List<LigneEmployePhase> affectations = ligneEmployePhaseRepo.findAll().stream()
                            .filter(a -> a.getEmploye() != null &&
                                    a.getEmploye().getId() == employeId)
                            .toList();
                    return ResponseEntity.ok(affectations);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/phases/{phaseId}/employees/{employeId}")
    public ResponseEntity<LigneEmployePhase> getAffectation(
            @PathVariable Integer phaseId,
            @PathVariable Integer employeId) {

        LigneEmployePhase affectation = ligneEmployePhaseRepo.findAll().stream()
                .filter(a -> a.getEmploye() != null &&
                        a.getPhase() != null &&
                        a.getEmploye().getId() == employeId &&
                        a.getPhase().getId() == phaseId)
                .findFirst()
                .orElse(null);

        if (affectation != null) {
            return ResponseEntity.ok(affectation);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/phases/{phaseId}/employees/{employeId}")
    public ResponseEntity<LigneEmployePhase> updateAffectation(
            @PathVariable Integer phaseId,
            @PathVariable Integer employeId,
            @RequestBody LigneEmployePhase affectation) {

        // Chercher l'affectation existante par employeId et phaseId
        LigneEmployePhase existingAffectation = ligneEmployePhaseRepo.findAll().stream()
                .filter(a -> a.getEmploye() != null &&
                        a.getPhase() != null &&
                        a.getEmploye().getId() == employeId &&
                        a.getPhase().getId() == phaseId)
                .findFirst()
                .orElse(null);

        if (existingAffectation == null) {
            return ResponseEntity.notFound().build();
        }

        return phaseRepo.findById(phaseId)
                .map(phase -> {
                    // Vérifier que les dates sont dans l'intervalle de la phase
                    if (affectation.getDateDebut().isBefore(phase.getDateDebut()) ||
                            affectation.getDateFin().isAfter(phase.getDateFin())) {
                        throw new RuntimeException("Les dates d'affectation doivent être comprises dans les dates de la phase");
                    }

                    existingAffectation.setDateDebut(affectation.getDateDebut());
                    existingAffectation.setDateFin(affectation.getDateFin());

                    LigneEmployePhase updatedAffectation = ligneEmployePhaseRepo.save(existingAffectation);
                    return ResponseEntity.ok(updatedAffectation);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/phases/{phaseId}/employees/{employeId}")
    public ResponseEntity<Void> deleteAffectation(
            @PathVariable Integer phaseId,
            @PathVariable Integer employeId) {

        // Chercher l'affectation à supprimer
        LigneEmployePhase affectation = ligneEmployePhaseRepo.findAll().stream()
                .filter(a -> a.getEmploye() != null &&
                        a.getPhase() != null &&
                        a.getEmploye().getId() == employeId &&
                        a.getPhase().getId() == phaseId)
                .findFirst()
                .orElse(null);

        if (affectation != null) {
            ligneEmployePhaseRepo.delete(affectation);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/affectations")
    public List<LigneEmployePhase> getAll() {
        return ligneEmployePhaseRepo.findAll();
    }
}