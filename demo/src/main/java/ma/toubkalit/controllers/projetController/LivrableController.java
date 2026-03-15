package ma.toubkalit.controllers.projetController;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.dto.LivrableDTO;
import ma.toubkalit.mappers.LivrableMapper;
import ma.toubkalit.services.projetService.LivrableService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LivrableController {

    private final LivrableService livrableService;
    private final LivrableMapper livrableMapper;

    @PostMapping("/phases/{phaseId}/livrables")
    @PreAuthorize("hasRole('CHEF_PROJET')")
    public ResponseEntity<LivrableDTO> create(@PathVariable Integer phaseId, @RequestBody LivrableDTO dto) {
        dto.setPhaseId(phaseId);
        return new ResponseEntity<>(
                livrableMapper.toDTO(livrableService.saveLivrable(livrableMapper.toEntity(dto))),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/phases/{phaseId}/livrables")
    @PreAuthorize("hasAnyRole('CHEF_PROJET', 'DIRECTEUR', 'ADMIN')")
    public List<LivrableDTO> getByPhaseId(@PathVariable Integer phaseId) {
        return livrableService.getAllLivrables().stream()
                .filter(l -> l.getPhase() != null && l.getPhase().getId().equals(phaseId))
                .map(livrableMapper::toDTO)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/livrables/{id}")
    @PreAuthorize("hasRole('CHEF_PROJET')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        livrableService.deleteLivrable(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/livrables/{id}/download")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> download(@PathVariable Integer id) {
        // Logique de téléchargement simulée
        return ResponseEntity.ok("Téléchargement du livrable " + id + " à implémenter.");
    }
}