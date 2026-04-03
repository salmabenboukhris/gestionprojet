package ma.toubkalit.suiviprojet.controllers;

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
    public ProjetResponseDto update(@PathVariable Long id,
                                    @Valid @RequestBody ProjetRequestDto requestDto) {
        return projetService.update(id, requestDto);
    }

    @GetMapping("/{id}")
    public ProjetResponseDto getById(@PathVariable Long id) {
        return projetService.getById(id);
    }

    @GetMapping
    public List<ProjetResponseDto> getAll(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) Long organismeId,
            @RequestParam(required = false) Long chefProjetId
    ) {
        return projetService.getAll(code, nom, organismeId, chefProjetId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        projetService.delete(id);
    }

    @GetMapping("/{id}/resume")
    public ProjetResumeDto getResume(@PathVariable Long id) {
        return projetService.getResume(id);
    }
}