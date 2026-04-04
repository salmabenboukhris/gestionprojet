package ma.toubkalit.suiviprojet.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ma.toubkalit.suiviprojet.dto.document.DocumentDownloadDto;
import ma.toubkalit.suiviprojet.dto.document.DocumentRequestDto;
import ma.toubkalit.suiviprojet.dto.document.DocumentResponseDto;
import ma.toubkalit.suiviprojet.services.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Documents", description = "Gestion des documents de projets (ADMIN, CHEF_PROJET, SECRETAIRE)")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/api/projets/{projetId}/documents")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Ajouter un document à un projet [ADMIN, CHEF_PROJET, SECRETAIRE]")
    public DocumentResponseDto create(@PathVariable("projetId") Long projetId,
                                      @Valid @RequestBody DocumentRequestDto requestDto) {

        return documentService.create(projetId, requestDto);
    }

    @GetMapping("/api/projets/{projetId}/documents")
    @Operation(summary = "Lister les documents d'un projet [ADMIN, CHEF_PROJET, SECRETAIRE]")
    public List<DocumentResponseDto> getByProjetId(@PathVariable("projetId") Long projetId) {

        return documentService.getByProjetId(projetId);
    }

    @GetMapping("/api/documents/{id}")
    @Operation(summary = "Récupérer un document par ID [ADMIN, CHEF_PROJET, SECRETAIRE]")
    public DocumentResponseDto getById(@PathVariable("id") Long id) {

        return documentService.getById(id);
    }

    @PutMapping("/api/documents/{id}")
    @Operation(summary = "Mettre à jour un document [ADMIN, CHEF_PROJET, SECRETAIRE]")
    public DocumentResponseDto update(@PathVariable("id") Long id,
                                      @Valid @RequestBody DocumentRequestDto requestDto) {

        return documentService.update(id, requestDto);
    }

    @DeleteMapping("/api/documents/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un document [ADMIN, CHEF_PROJET, SECRETAIRE]")
    public void delete(@PathVariable("id") Long id) {

        documentService.delete(id);
    }

    @GetMapping("/api/documents/{id}/download")
    @Operation(summary = "Télécharger un document [ADMIN, CHEF_PROJET, SECRETAIRE]")
    public DocumentDownloadDto download(@PathVariable("id") Long id) {

        return documentService.download(id);
    }
}