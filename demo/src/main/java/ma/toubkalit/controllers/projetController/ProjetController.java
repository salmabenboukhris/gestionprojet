package ma.toubkalit.controllers.projetController;


import lombok.RequiredArgsConstructor;
import ma.toubkalit.entity.projet.Projet;
import ma.toubkalit.services.projetService.ProjetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projets")
@RequiredArgsConstructor
public class ProjetController {

    private final ProjetService projetService;

    @PostMapping
    public ResponseEntity<Projet> create(@RequestBody Projet projet) {
        Projet savedProjet = projetService.saveProjet(projet);
        return new ResponseEntity<>(savedProjet, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Projet> update(@PathVariable Integer id, @RequestBody Projet projet) {
        Projet updatedProjet = projetService.updateProjet(id, projet);
        return ResponseEntity.ok(updatedProjet);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Projet> getById(@PathVariable Integer id) {
        Projet projet = projetService.getProjetById(id);
        return ResponseEntity.ok(projet);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Projet> getByCode(@PathVariable String code) {
        Projet projet = projetService.getProjetByCode(code);
        return ResponseEntity.ok(projet);
    }

    @GetMapping
    public List<Projet> getAll() {
        return projetService.getAllProjets();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        projetService.deleteProjet(id);
        return ResponseEntity.noContent().build();
    }
}