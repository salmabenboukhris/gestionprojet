package ma.toubkalit.suiviprojet.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ma.toubkalit.suiviprojet.dto.projet.ProjetRequestDto;
import ma.toubkalit.suiviprojet.dto.projet.ProjetResponseDto;
import ma.toubkalit.suiviprojet.dto.projet.ProjetResumeDto;
import ma.toubkalit.suiviprojet.services.ProjetService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projets")
@Tag(name = "Projets", description = "Gestion des projets (ADMIN, SECRETAIRE, CHEF_PROJET)")
public class ProjetController {

    private final ProjetService projetService;

    public ProjetController(ProjetService projetService) {
        this.projetService = projetService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjetResponseDto create(@Valid @RequestBody ProjetRequestDto requestDto) {
        return projetService.create(requestDto);
    }

    @PutMapping("/{id}")
    public ProjetResponseDto update(@PathVariable("id") Long id,
                                    @Valid @RequestBody ProjetRequestDto requestDto) {

        return projetService.update(id, requestDto);
    }

    @GetMapping("/{id}")
    public ProjetResponseDto getById(@PathVariable("id") Long id) {

        return projetService.getById(id);
    }

    @GetMapping
    public List<ProjetResponseDto> getAll(
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "nom", required = false) String nom,
            @RequestParam(name = "organismeId", required = false) Long organismeId,
            @RequestParam(name = "chefProjetId", required = false) Long chefProjetId
    ) {

        return projetService.getAll(code, nom, organismeId, chefProjetId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {

        projetService.delete(id);
    }

    @GetMapping("/{id}/resume")
    public ProjetResumeDto getResume(@PathVariable("id") Long id) {

        return projetService.getResume(id);
    }
}