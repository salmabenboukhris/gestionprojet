package ma.toubkalit.controllers.projetController;


import lombok.RequiredArgsConstructor;
import ma.toubkalit.entity.projet.Organisme;
import ma.toubkalit.repositories.projetRepo.OrganismeRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organismes")
@RequiredArgsConstructor
public class OrganismeController {

    private final OrganismeRepo organismeRepo;

    @PostMapping
    public ResponseEntity<Organisme> create(@RequestBody Organisme organisme) {
        // Vérifier l'unicité du code
        if (organisme.getCode() != null) {
            organismeRepo.findByCode(organisme.getCode())
                    .ifPresent(o -> {
                        throw new RuntimeException("Un organisme avec ce code existe déjà");
                    });
        }

        // Vérifier l'unicité du nom
        if (organisme.getNom() != null) {
            organismeRepo.findByNom(organisme.getNom())
                    .ifPresent(o -> {
                        throw new RuntimeException("Un organisme avec ce nom existe déjà");
                    });
        }

        Organisme savedOrganisme = organismeRepo.save(organisme);
        return new ResponseEntity<>(savedOrganisme, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organisme> getById(@PathVariable Integer id) {
        return organismeRepo.findById(id)
                .map(organisme -> ResponseEntity.ok(organisme))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Organisme> getByCode(@PathVariable String code) {
        return organismeRepo.findByCode(code)
                .map(organisme -> ResponseEntity.ok(organisme))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nom/{nom}")
    public ResponseEntity<Organisme> getByNom(@PathVariable String nom) {
        return organismeRepo.findByNom(nom)
                .map(organisme -> ResponseEntity.ok(organisme))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Organisme> getAll() {
        return organismeRepo.findAll();
    }

    @GetMapping("/recherche")
    public List<Organisme> search(@RequestParam(required = false) String nom,
                                  @RequestParam(required = false) String code) {
        if (nom != null && code != null) {
            // Recherche par nom et code (filtrage manuel car pas de méthode dédiée)
            return organismeRepo.findAll().stream()
                    .filter(o -> o.getNom() != null && o.getNom().toLowerCase().contains(nom.toLowerCase()) &&
                            o.getCode() != null && o.getCode().toLowerCase().contains(code.toLowerCase()))
                    .toList();
        } else if (nom != null) {
            // Recherche par nom (contient)
            return organismeRepo.findAll().stream()
                    .filter(o -> o.getNom() != null && o.getNom().toLowerCase().contains(nom.toLowerCase()))
                    .toList();
        } else if (code != null) {
            // Recherche par code (contient)
            return organismeRepo.findAll().stream()
                    .filter(o -> o.getCode() != null && o.getCode().toLowerCase().contains(code.toLowerCase()))
                    .toList();
        }
        return organismeRepo.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Organisme> update(@PathVariable Integer id, @RequestBody Organisme organisme) {
        return organismeRepo.findById(id)
                .map(existingOrganisme -> {
                    // Vérifier l'unicité du code si modifié
                    if (organisme.getCode() != null &&
                            !organisme.getCode().equals(existingOrganisme.getCode())) {
                        organismeRepo.findByCode(organisme.getCode())
                                .ifPresent(o -> {
                                    throw new RuntimeException("Un organisme avec ce code existe déjà");
                                });
                        existingOrganisme.setCode(organisme.getCode());
                    }

                    // Vérifier l'unicité du nom si modifié
                    if (organisme.getNom() != null &&
                            !organisme.getNom().equals(existingOrganisme.getNom())) {
                        organismeRepo.findByNom(organisme.getNom())
                                .ifPresent(o -> {
                                    throw new RuntimeException("Un organisme avec ce nom existe déjà");
                                });
                        existingOrganisme.setNom(organisme.getNom());
                    }

                    // Mettre à jour les autres champs
                    if (organisme.getAdresse() != null) existingOrganisme.setAdresse(organisme.getAdresse());
                    if (organisme.getTelephone() != null) existingOrganisme.setTelephone(organisme.getTelephone());
                    if (organisme.getNomContact() != null) existingOrganisme.setNomContact(organisme.getNomContact());
                    if (organisme.getEmailContact() != null) existingOrganisme.setEmailContact(organisme.getEmailContact());
                    if (organisme.getSiteWeb() != null) existingOrganisme.setSiteWeb(organisme.getSiteWeb());

                    Organisme updatedOrganisme = organismeRepo.save(existingOrganisme);
                    return ResponseEntity.ok(updatedOrganisme);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (organismeRepo.existsById(id)) {
            organismeRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}