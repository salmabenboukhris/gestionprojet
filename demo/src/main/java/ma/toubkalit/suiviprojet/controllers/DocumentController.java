package ma.toubkalit.suiviprojet.controllers;

import jakarta.validation.Valid;
import ma.toubkalit.suiviprojet.dto.document.DocumentDownloadDto;
import ma.toubkalit.suiviprojet.dto.document.DocumentRequestDto;
import ma.toubkalit.suiviprojet.dto.document.DocumentResponseDto;
import ma.toubkalit.suiviprojet.services.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/api/projets/{projetId}/documents")
    @ResponseStatus(HttpStatus.CREATED)
    public DocumentResponseDto create(@PathVariable Long projetId,
                                      @Valid @RequestBody DocumentRequestDto requestDto) {
        return documentService.create(projetId, requestDto);
    }

    @GetMapping("/api/projets/{projetId}/documents")
    public List<DocumentResponseDto> getByProjetId(@PathVariable Long projetId) {
        return documentService.getByProjetId(projetId);
    }

    @GetMapping("/api/documents/{id}")
    public DocumentResponseDto getById(@PathVariable Long id) {
        return documentService.getById(id);
    }

    @PutMapping("/api/documents/{id}")
    public DocumentResponseDto update(@PathVariable Long id,
                                      @Valid @RequestBody DocumentRequestDto requestDto) {
        return documentService.update(id, requestDto);
    }

    @DeleteMapping("/api/documents/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        documentService.delete(id);
    }

    @GetMapping("/api/documents/{id}/download")
    public DocumentDownloadDto download(@PathVariable Long id) {
        return documentService.download(id);
    }
}