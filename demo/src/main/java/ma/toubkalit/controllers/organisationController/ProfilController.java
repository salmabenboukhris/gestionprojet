package ma.toubkalit.controllers.organisationController;


import lombok.RequiredArgsConstructor;
import ma.toubkalit.entity.organisation.Profil;
import ma.toubkalit.repositories.organisationRepo.ProfilRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profils")
@RequiredArgsConstructor
 public class ProfilController {

    private final ProfilRepo profilRepo;

    // POST - Créer un profil
    @PostMapping
    public ResponseEntity<Profil> create(@RequestBody Profil profil) {
        Profil savedProfil = profilRepo.save(profil);
        return new ResponseEntity<>(savedProfil, HttpStatus.CREATED); // 201 CREATED
    }

    // GET - Récupérer un profil par ID
    @GetMapping("/{id}")
    public ResponseEntity<Profil> getById(@PathVariable Integer id) {
        return profilRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET - Récupérer tous les profils
    @GetMapping
    public List<Profil> getAll() {
        return profilRepo.findAll();
    }

    // PUT - Modifier un profil
    @PutMapping("/{id}")
    public ResponseEntity<Profil> update(@PathVariable Integer id, @RequestBody Profil profil) {
        return profilRepo.findById(id)
                .map(existingProfil -> {
                    existingProfil.setCode(profil.getCode());
                    existingProfil.setLibelle(profil.getLibelle());
                    Profil updatedProfil = profilRepo.save(existingProfil);
                    return ResponseEntity.ok(updatedProfil);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE - Supprimer un profil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (profilRepo.existsById(id)) {
            profilRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}