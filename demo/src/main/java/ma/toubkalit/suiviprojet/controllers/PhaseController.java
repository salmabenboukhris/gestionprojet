package ma.toubkalit.suiviprojet.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ma.toubkalit.suiviprojet.dto.phase.PhaseRequestDto;
import ma.toubkalit.suiviprojet.dto.phase.PhaseResponseDto;
import ma.toubkalit.suiviprojet.dto.phase.PhaseStatusUpdateDto;
import ma.toubkalit.suiviprojet.services.PhaseService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Phases", description = "Gestion des phases de projets (ADMIN, CHEF_PROJET)")
public class PhaseController {

    private final PhaseService phaseService;

    public PhaseController(PhaseService phaseService) {
        this.phaseService = phaseService;
    }

    @PostMapping("/api/projets/{projetId}/phases")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer une nouvelle phase dans un projet [ADMIN, CHEF_PROJET]")
    public PhaseResponseDto create(@PathVariable("projetId") Long projetId,
                                   @Valid @RequestBody PhaseRequestDto requestDto) {

        return phaseService.create(projetId, requestDto);
    }

    @GetMapping("/api/projets/{projetId}/phases")
    @Operation(summary = "Lister les phases d'un projet [ADMIN, CHEF_PROJET, SECRETAIRE]")
    public List<PhaseResponseDto> getByProjetId(@PathVariable("projetId") Long projetId) {

        return phaseService.getByProjetId(projetId);
    }

    @GetMapping("/api/phases/{id}")
    @Operation(summary = "Récupérer une phase par son ID [ADMIN, CHEF_PROJET]")
    public PhaseResponseDto getById(@PathVariable("id") Long id) {

        return phaseService.getById(id);
    }

    @PutMapping("/api/phases/{id}")
    @Operation(summary = "Mettre à jour une phase [ADMIN, CHEF_PROJET]")
    public PhaseResponseDto update(@PathVariable("id") Long id,
                                   @Valid @RequestBody PhaseRequestDto requestDto) {

        return phaseService.update(id, requestDto);
    }

    @DeleteMapping("/api/phases/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer une phase [ADMIN, CHEF_PROJET]")
    public void delete(@PathVariable("id") Long id) {

        phaseService.delete(id);
    }

    @PatchMapping("/api/phases/{id}/realisation")
    @Operation(summary = "Mettre à jour l'état de réalisation d'une phase [ADMIN, CHEF_PROJET]")
    public PhaseResponseDto updateEtatRealisation(@PathVariable("id") Long id,
                                                  @Valid @RequestBody PhaseStatusUpdateDto dto) {

        return phaseService.updateEtatRealisation(id, dto.getValue());
    }

    @PatchMapping("/api/phases/{id}/facturation")
    @Operation(summary = "Mettre à jour l'état de facturation d'une phase [ADMIN, COMPTABLE]")
    public PhaseResponseDto updateEtatFacturation(@PathVariable("id") Long id,
                                                  @Valid @RequestBody PhaseStatusUpdateDto dto) {

        return phaseService.updateEtatFacturation(id, dto.getValue());
    }

    @PatchMapping("/api/phases/{id}/paiement")
    @Operation(summary = "Mettre à jour l'état de paiement d'une phase [ADMIN, COMPTABLE]")
    public PhaseResponseDto updateEtatPaiement(@PathVariable("id") Long id,
                                               @Valid @RequestBody PhaseStatusUpdateDto dto) {

        return phaseService.updateEtatPaiement(id, dto.getValue());
    }
}