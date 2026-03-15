package ma.toubkalit.controllers.projetController;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.dto.AffectationDTO;
import ma.toubkalit.entity.projet.AffectationId;
import ma.toubkalit.mappers.AffectationMapper;
import ma.toubkalit.services.projetService.AffectationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CHEF_PROJET')")
public class AffectationController {

    private final AffectationService affectationService;
    private final AffectationMapper affectationMapper;

    @PostMapping("/phases/{phaseId}/employes/{employeId}")
    public ResponseEntity<AffectationDTO> create(
            @PathVariable Integer phaseId,
            @PathVariable Integer employeId,
            @RequestBody AffectationDTO dto) {
        dto.setPhaseId(phaseId);
        dto.setEmployeId(employeId);
        return new ResponseEntity<>(
                affectationMapper.toDTO(affectationService.saveAffectation(affectationMapper.toEntity(dto))),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/phases/{phaseId}/employes")
    public List<AffectationDTO> getByPhase(@PathVariable Integer phaseId) {
        return affectationService.getAffectationsByPhase(phaseId).stream()
                .map(affectationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/phases/{phaseId}/employes/{employeId}")
    public ResponseEntity<AffectationDTO> getById(@PathVariable Integer phaseId, @PathVariable Integer employeId) {
        AffectationId id = new AffectationId(employeId, phaseId);
        return ResponseEntity.ok(affectationMapper.toDTO(affectationService.getAffectationById(id)));
    }

    @PutMapping("/phases/{phaseId}/employes/{employeId}")
    public ResponseEntity<AffectationDTO> update(
            @PathVariable Integer phaseId,
            @PathVariable Integer employeId,
            @RequestBody AffectationDTO dto) {
        AffectationId id = new AffectationId(employeId, phaseId);
        return ResponseEntity.ok(
                affectationMapper.toDTO(affectationService.updateAffectation(id, affectationMapper.toEntity(dto)))
        );
    }

    @DeleteMapping("/phases/{phaseId}/employes/{employeId}")
    public ResponseEntity<Void> delete(@PathVariable Integer phaseId, @PathVariable Integer employeId) {
        AffectationId id = new AffectationId(employeId, phaseId);
        affectationService.deleteAffectation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employes/{employeId}/phases")
    public List<AffectationDTO> getByEmploye(@PathVariable Integer employeId) {
        return affectationService.getAffectationsByEmploye(employeId).stream()
                .map(affectationMapper::toDTO)
                .collect(Collectors.toList());
    }
}
