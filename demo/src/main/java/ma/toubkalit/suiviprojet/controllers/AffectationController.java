package ma.toubkalit.suiviprojet.controllers;

import jakarta.validation.Valid;
import ma.toubkalit.suiviprojet.dto.affectation.AffectationRequestDto;
import ma.toubkalit.suiviprojet.dto.affectation.AffectationResponseDto;
import ma.toubkalit.suiviprojet.services.AffectationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AffectationController {

    private final AffectationService affectationService;

    public AffectationController(AffectationService affectationService) {
        this.affectationService = affectationService;
    }

    @PostMapping("/api/phases/{phaseId}/employes/{employeId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AffectationResponseDto create(@PathVariable Long phaseId,
                                         @PathVariable Long employeId,
                                         @Valid @RequestBody AffectationRequestDto requestDto) {
        return affectationService.create(phaseId, employeId, requestDto);
    }

    @GetMapping("/api/phases/{phaseId}/employes")
    public List<AffectationResponseDto> getEmployesByPhase(@PathVariable Long phaseId) {
        return affectationService.getEmployesByPhase(phaseId);
    }

    @GetMapping("/api/phases/{phaseId}/employes/{employeId}")
    public AffectationResponseDto getByPhaseAndEmploye(@PathVariable Long phaseId,
                                                       @PathVariable Long employeId) {
        return affectationService.getByPhaseAndEmploye(phaseId, employeId);
    }

    @PutMapping("/api/phases/{phaseId}/employes/{employeId}")
    public AffectationResponseDto update(@PathVariable Long phaseId,
                                         @PathVariable Long employeId,
                                         @Valid @RequestBody AffectationRequestDto requestDto) {
        return affectationService.update(phaseId, employeId, requestDto);
    }

    @DeleteMapping("/api/phases/{phaseId}/employes/{employeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long phaseId,
                       @PathVariable Long employeId) {
        affectationService.delete(phaseId, employeId);
    }

    @GetMapping("/api/employes/{employeId}/phases")
    public List<AffectationResponseDto> getPhasesByEmploye(@PathVariable Long employeId) {
        return affectationService.getPhasesByEmploye(employeId);
    }
}