package ma.toubkalit.controllers.projetController;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.dto.ProjetDTO;
import ma.toubkalit.mappers.ProjetMapper;
import ma.toubkalit.services.projetService.ProjetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projets")
@RequiredArgsConstructor
public class ProjetController {

    private final ProjetService projetService;
    private final ProjetMapper projetMapper;

    @PostMapping
    @PreAuthorize("hasRole('SECRETAIRE')")
    public ResponseEntity<ProjetDTO> create(@RequestBody ProjetDTO dto) {
        return new ResponseEntity<>(
                projetMapper.toDTO(projetService.saveProjet(projetMapper.toEntity(dto))),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SECRETAIRE', 'DIRECTEUR')")
    public ResponseEntity<ProjetDTO> update(@PathVariable Integer id, @RequestBody ProjetDTO dto) {
        return ResponseEntity.ok(
                projetMapper.toDTO(projetService.updateProjet(id, projetMapper.toEntity(dto)))
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SECRETAIRE', 'DIRECTEUR', 'CHEF_PROJET', 'COMPTABLE', 'ADMIN', 'EMPLOYE')")
    public ResponseEntity<ProjetDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(projetMapper.toDTO(projetService.getProjetById(id)));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<ProjetDTO> getAll() {
        return projetService.getAllProjets().stream()
                .map(projetMapper::toDTO)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DIRECTEUR')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        projetService.deleteProjet(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/resume")
    @PreAuthorize("hasRole('DIRECTEUR')")
    public ResponseEntity<?> getResume(@PathVariable Integer id) {
        // Logique à implémenter pour le résumé financier/état
        return ResponseEntity.ok("Résumé du projet " + id + " à implémenter.");
    }
}