package ma.toubkalit.controllers.projetController;


import lombok.RequiredArgsConstructor;
import ma.toubkalit.entity.projet.Livrable;
import ma.toubkalit.repositories.projetRepo.LivrableRepo;
import ma.toubkalit.repositories.projetRepo.PhaseRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LivrableController {

    private final LivrableRepo livrableRepo;
    private final PhaseRepo phaseRepo;

    @PostMapping("/phases/{phaseId}/livrables")
    public ResponseEntity<Livrable> create(@PathVariable Integer phaseId, @RequestBody Livrable livrable) {
        return phaseRepo.findById(phaseId)
                .map(phase -> {
                    livrable.setPhase(phase);
                    Livrable savedLivrable = livrableRepo.save(livrable);
                    return new ResponseEntity<>(savedLivrable, HttpStatus.CREATED);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/livrables/{id}")
    public ResponseEntity<Livrable> getById(@PathVariable Integer id) {
        return livrableRepo.findById(id)
                .map(livrable -> ResponseEntity.ok(livrable))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/phases/{phaseId}/livrables")
    public ResponseEntity<List<Livrable>> getByPhaseId(@PathVariable Integer phaseId) {
        return phaseRepo.findById(phaseId)
                .map(phase -> {
                    List<Livrable> livrables = livrableRepo.findAll().stream()
                            .filter(l -> l.getPhase() != null &&
                                    l.getPhase().getId() == phaseId)
                            .toList();
                    return ResponseEntity.ok(livrables);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/livrables")
    public List<Livrable> getAll() {
        return livrableRepo.findAll();
    }

    @GetMapping("/livrables/code/{code}")
    public ResponseEntity<Livrable> getByCode(@PathVariable String code) {
        List<Livrable> livrables = livrableRepo.findAll();
        return livrables.stream()
                .filter(l -> code.equals(l.getCode()))
                .findFirst()
                .map(livrable -> ResponseEntity.ok(livrable))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/livrables/{id}")
    public ResponseEntity<Livrable> update(@PathVariable Integer id, @RequestBody Livrable livrable) {
        return livrableRepo.findById(id)
                .map(existingLivrable -> {
                    existingLivrable.setCode(livrable.getCode());
                    existingLivrable.setLibelle(livrable.getLibelle());
                    existingLivrable.setDescription(livrable.getDescription());
                    existingLivrable.setChemin(livrable.getChemin());

                    // Ne pas changer la phase

                    Livrable updatedLivrable = livrableRepo.save(existingLivrable);
                    return ResponseEntity.ok(updatedLivrable);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/livrables/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (livrableRepo.existsById(id)) {
            livrableRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint pour télécharger le livrable (à implémenter plus tard)
    @GetMapping("/livrables/{id}/download")
    public ResponseEntity<String> download(@PathVariable Integer id) {
        return livrableRepo.findById(id)
                .map(livrable -> {
                    // Logique de téléchargement à implémenter
                    return ResponseEntity.ok("Téléchargement du livrable : " + livrable.getChemin());
                })
                .orElse(ResponseEntity.notFound().build());
    }
}