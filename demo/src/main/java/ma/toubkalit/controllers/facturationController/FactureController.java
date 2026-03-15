package ma.toubkalit.controllers.facturationController;


import lombok.RequiredArgsConstructor;
import ma.toubkalit.entity.facturation.Facture;
import ma.toubkalit.repositories.facturationRepo.FactureRepo;
import ma.toubkalit.repositories.projetRepo.PhaseRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/factures")
@RequiredArgsConstructor
public class FactureController {

    private final FactureRepo factureRepo;
    private final PhaseRepo phaseRepo;

    @PostMapping
    public ResponseEntity<Facture> create(@RequestBody Facture facture) {
        if (facture.getPhase() != null && facture.getPhase().getId() != 0) {
            phaseRepo.findById(facture.getPhase().getId())
                    .orElseThrow(() -> new RuntimeException("Phase non trouvée"));
        }
        Facture savedFacture = factureRepo.save(facture);
        return new ResponseEntity<>(savedFacture, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Facture> getById(@PathVariable Integer id) {
        return factureRepo.findById(id)
                .map(facture -> ResponseEntity.ok(facture))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Facture> getByCode(@PathVariable String code) {
        return factureRepo.findByCode(code)
                .map(facture -> ResponseEntity.ok(facture))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Facture> getAll() {
        return factureRepo.findAll();
    }

    @GetMapping("/phase/{phaseId}")
    public ResponseEntity<Facture> getByPhaseId(@PathVariable Integer phaseId) {
        List<Facture> factures = factureRepo.findAll();
        return factures.stream()
                .filter(facture -> facture.getPhase() != null &&
                        facture.getPhase().getId() == phaseId)
                .findFirst()
                .map(facture -> ResponseEntity.ok(facture))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Facture> update(@PathVariable Integer id, @RequestBody Facture facture) {
        return factureRepo.findById(id)
                .map(existingFacture -> {
                    existingFacture.setCode(facture.getCode());
                    existingFacture.setDateFacture(facture.getDateFacture());

                    // Mettre à jour la phase si fournie
                    if (facture.getPhase() != null && facture.getPhase().getId() != 0) {
                        phaseRepo.findById(facture.getPhase().getId())
                                .ifPresent(existingFacture::setPhase);
                    }

                    Facture updatedFacture = factureRepo.save(existingFacture);
                    return ResponseEntity.ok(updatedFacture);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (factureRepo.existsById(id)) {
            factureRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}