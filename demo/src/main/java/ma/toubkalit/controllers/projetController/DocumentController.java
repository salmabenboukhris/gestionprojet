package ma.toubkalit.controllers.projetController;


import lombok.RequiredArgsConstructor;
import ma.toubkalit.entity.projet.Document;
import ma.toubkalit.repositories.projetRepo.DocumentRepo;
import ma.toubkalit.repositories.projetRepo.ProjetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentRepo documentRepo;
    private final ProjetRepository projetRepo;

    @PostMapping("/projets/{projetId}/documents")
    public ResponseEntity<Document> create(@PathVariable Integer projetId, @RequestBody Document document) {
        return projetRepo.findById(projetId)
                .map(projet -> {
                    document.setProjet(projet);
                    Document savedDocument = documentRepo.save(document);
                    return new ResponseEntity<>(savedDocument, HttpStatus.CREATED);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/documents/{id}")
    public ResponseEntity<Document> getById(@PathVariable Integer id) {
        return documentRepo.findById(id)
                .map(document -> ResponseEntity.ok(document))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/projets/{projetId}/documents")
    public ResponseEntity<List<Document>> getByProjetId(@PathVariable Integer projetId) {
        return projetRepo.findById(projetId)
                .map(projet -> {
                    List<Document> documents = documentRepo.findAll().stream()
                            .filter(doc -> doc.getProjet() != null &&
                                    doc.getProjet().getId() == projetId)
                            .toList();
                    return ResponseEntity.ok(documents);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/documents")
    public List<Document> getAll() {
        return documentRepo.findAll();
    }

    @GetMapping("/documents/code/{code}")
    public ResponseEntity<Document> getByCode(@PathVariable String code) {
        List<Document> documents = documentRepo.findAll();
        return documents.stream()
                .filter(doc -> code.equals(doc.getCode()))
                .findFirst()
                .map(document -> ResponseEntity.ok(document))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/documents/{id}")
    public ResponseEntity<Document> update(@PathVariable Integer id, @RequestBody Document document) {
        return documentRepo.findById(id)
                .map(existingDocument -> {
                    existingDocument.setCode(document.getCode());
                    existingDocument.setLibelle(document.getLibelle());
                    existingDocument.setDescription(document.getDescription());
                    existingDocument.setChemin(document.getChemin());


                    Document updatedDocument = documentRepo.save(existingDocument);
                    return ResponseEntity.ok(updatedDocument);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/documents/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (documentRepo.existsById(id)) {
            documentRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/documents/{id}/download")
    public ResponseEntity<String> download(@PathVariable Integer id) {
        return documentRepo.findById(id)
                .map(document -> {
                    // Logique de téléchargement à implémenter
                    return ResponseEntity.ok("Téléchargement du document : " + document.getChemin());
                })
                .orElse(ResponseEntity.notFound().build());
    }
}