package ma.toubkalit.suiviprojet.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ma.toubkalit.suiviprojet.dto.affectation.AffectationRequestDto;
import ma.toubkalit.suiviprojet.dto.affectation.AffectationResponseDto;
import ma.toubkalit.suiviprojet.services.AffectationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Affectations", description = "Gestion des affectations employés-phases (ADMIN, CHEF_PROJET)")
public class AffectationController {

    private final AffectationService affectationService;

    public AffectationController(AffectationService affectationService) {
        this.affectationService = affectationService;
    }

    @PostMapping("/api/phases/{phaseId}/employes/{employeId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Affecter un employé à une phase [ADMIN, CHEF_PROJET]")
    public AffectationResponseDto create(@PathVariable("phaseId") Long phaseId,
                                         @PathVariable("employeId") Long employeId,
                                         @Valid @RequestBody AffectationRequestDto requestDto) {

        return affectationService.create(phaseId, employeId, requestDto);
    }

    @GetMapping("/api/phases/{phaseId}/employes")
    @Operation(summary = "Lister les employés affectés à une phase [ADMIN, CHEF_PROJET]")
    public List<AffectationResponseDto> getEmployesByPhase(@PathVariable("phaseId") Long phaseId) {

        return affectationService.getEmployesByPhase(phaseId);
    }

    @GetMapping("/api/phases/{phaseId}/employes/{employeId}")
    @Operation(summary = "Récupérer une affectation spécifique [ADMIN, CHEF_PROJET]")
    public AffectationResponseDto getByPhaseAndEmploye(@PathVariable("phaseId") Long phaseId,
                                                       @PathVariable("employeId") Long employeId) {

        return affectationService.getByPhaseAndEmploye(phaseId, employeId);
    }

    @PutMapping("/api/phases/{phaseId}/employes/{employeId}")
    @Operation(summary = "Mettre à jour une affectation [ADMIN, CHEF_PROJET]")
    public AffectationResponseDto update(@PathVariable("phaseId") Long phaseId,
                                         @PathVariable("employeId") Long employeId,
                                         @Valid @RequestBody AffectationRequestDto requestDto) {

        return affectationService.update(phaseId, employeId, requestDto);
    }

    @DeleteMapping("/api/phases/{phaseId}/employes/{employeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer une affectation [ADMIN, CHEF_PROJET]")
    public void delete(@PathVariable("phaseId") Long phaseId,
                       @PathVariable("employeId") Long employeId) {

        affectationService.delete(phaseId, employeId);
    }

    @GetMapping("/api/employes/{employeId}/phases")
    @Operation(summary = "Lister les phases d'un employé [ADMIN, CHEF_PROJET, SECRETAIRE]")
    public List<AffectationResponseDto> getPhasesByEmploye(@PathVariable("employeId") Long employeId) {

        return affectationService.getPhasesByEmploye(employeId);
    }
}