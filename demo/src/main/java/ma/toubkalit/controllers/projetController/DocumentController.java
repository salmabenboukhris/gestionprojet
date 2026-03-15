package ma.toubkalit.controllers.projetController;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.dto.DocumentDTO;
import ma.toubkalit.mappers.DocumentMapper;
import ma.toubkalit.services.projetService.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentMapper documentMapper;

    @PostMapping("/projets/{projetId}/documents")
    @PreAuthorize("hasRole('SECRETAIRE')")
    public ResponseEntity<DocumentDTO> create(@PathVariable Integer projetId, @RequestBody DocumentDTO dto) {
        dto.setProjetId(projetId);
        return new ResponseEntity<>(
                documentMapper.toDTO(documentService.saveDocument(documentMapper.toEntity(dto))),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/projets/{projetId}/documents")
    @PreAuthorize("isAuthenticated()")
    public List<DocumentDTO> getByProjetId(@PathVariable Integer projetId) {
        return documentService.getAllDocuments().stream()
                .filter(doc -> doc.getProjet() != null && doc.getProjet().getId().equals(projetId))
                .map(documentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/documents/{id}/download")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> download(@PathVariable Integer id) {
        // Logique de téléchargement simulée
        return ResponseEntity.ok("Téléchargement du document " + id + " à implémenter.");
    }

    @DeleteMapping("/documents/{id}")
    @PreAuthorize("hasRole('SECRETAIRE')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}