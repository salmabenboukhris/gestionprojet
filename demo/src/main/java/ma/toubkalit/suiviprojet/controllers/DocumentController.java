package ma.toubkalit.suiviprojet.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ma.toubkalit.suiviprojet.dto.document.DocumentDownloadDto;
import ma.toubkalit.suiviprojet.dto.document.DocumentRequestDto;
import ma.toubkalit.suiviprojet.dto.document.DocumentResponseDto;
import ma.toubkalit.suiviprojet.services.DocumentService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @Operation(summary = "Ajouter un document (JSON) à un projet [ADMIN, CHEF_PROJET, SECRETAIRE]")
    public DocumentResponseDto create(@PathVariable("projetId") Long projetId,
                                      @Valid @RequestBody DocumentRequestDto requestDto) {

        return documentService.create(projetId, requestDto);
    }

    @PostMapping(value = "/api/projets/{projetId}/documents/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Uploader un fichier document pour un projet [ADMIN, CHEF_PROJET, SECRETAIRE]")
    public DocumentResponseDto upload(
            @PathVariable("projetId") Long projetId,
            @RequestParam("code") String code,
            @RequestParam("libelle") String libelle,
            @RequestParam("file") MultipartFile file) {

        return documentService.upload(projetId, code, libelle, file);
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
    public ResponseEntity<Resource> download(@PathVariable("id") Long id) {
        DocumentDownloadDto dto = documentService.download(id);

        if (dto.getChemin() == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            java.nio.file.Path filePath = java.nio.file.Paths.get(dto.getChemin());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            String filename = filePath.getFileName().toString();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + filename + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}