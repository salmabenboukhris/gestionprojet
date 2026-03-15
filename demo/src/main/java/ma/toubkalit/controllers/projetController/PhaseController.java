package ma.toubkalit.controllers.projetController;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.dto.PhaseDTO;
import ma.toubkalit.enums.EtatFacturation;
import ma.toubkalit.enums.EtatPaiement;
import ma.toubkalit.enums.EtatRealisation;
import ma.toubkalit.mappers.PhaseMapper;
import ma.toubkalit.services.projetService.PhaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PhaseController {

    private final PhaseService phaseService;
    private final PhaseMapper phaseMapper;

    @PostMapping("/projets/{projetId}/phases")
    @PreAuthorize("hasRole('CHEF_PROJET')")
    public ResponseEntity<PhaseDTO> create(@PathVariable Integer projetId, @RequestBody PhaseDTO dto) {
        dto.setProjetId(projetId);
        return new ResponseEntity<>(
                phaseMapper.toDTO(phaseService.savePhase(phaseMapper.toEntity(dto))),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/projets/{projetId}/phases")
    @PreAuthorize("hasAnyRole('CHEF_PROJET', 'DIRECTEUR', 'SECRETAIRE', 'ADMIN')")
    public List<PhaseDTO> getByProjetId(@PathVariable Integer projetId) {
        return phaseService.getAllPhases().stream()
                .filter(p -> p.getProjet() != null && p.getProjet().getId().equals(projetId))
                .map(phaseMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/phases/{id}")
    @PreAuthorize("hasAnyRole('CHEF_PROJET', 'DIRECTEUR', 'ADMIN')")
    public ResponseEntity<PhaseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(phaseMapper.toDTO(phaseService.getPhaseById(id)));
    }

    @PutMapping("/phases/{id}")
    @PreAuthorize("hasRole('CHEF_PROJET')")
    public ResponseEntity<PhaseDTO> update(@PathVariable Integer id, @RequestBody PhaseDTO dto) {
        return ResponseEntity.ok(
                phaseMapper.toDTO(phaseService.updatePhase(id, phaseMapper.toEntity(dto)))
        );
    }

    @PatchMapping("/phases/{id}/realisation")
    @PreAuthorize("hasRole('CHEF_PROJET')")
    public ResponseEntity<PhaseDTO> updateRealisation(@PathVariable Integer id, @RequestParam EtatRealisation etat) {
        return ResponseEntity.ok(phaseMapper.toDTO(phaseService.updateEtatRealisation(id, etat)));
    }

    @PatchMapping("/phases/{id}/facturation")
    @PreAuthorize("hasRole('COMPTABLE')")
    public ResponseEntity<PhaseDTO> updateFacturation(@PathVariable Integer id, @RequestParam EtatFacturation etat) {
        return ResponseEntity.ok(phaseMapper.toDTO(phaseService.updateEtatFacturation(id, etat)));
    }

    @PatchMapping("/phases/{id}/paiement")
    @PreAuthorize("hasRole('COMPTABLE')")
    public ResponseEntity<PhaseDTO> updatePaiement(@PathVariable Integer id, @RequestParam EtatPaiement etat) {
        return ResponseEntity.ok(phaseMapper.toDTO(phaseService.updateEtatPaiement(id, etat)));
    }

    @DeleteMapping("/phases/{id}")
    @PreAuthorize("hasRole('CHEF_PROJET')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        phaseService.deletePhase(id);
        return ResponseEntity.noContent().build();
    }
}
