package ma.toubkalit.suiviprojet.controllers;

import jakarta.validation.Valid;
import ma.toubkalit.suiviprojet.dto.phase.PhaseRequestDto;
import ma.toubkalit.suiviprojet.dto.phase.PhaseResponseDto;
import ma.toubkalit.suiviprojet.dto.phase.PhaseStatusUpdateDto;
import ma.toubkalit.suiviprojet.services.PhaseService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PhaseController {

    private final PhaseService phaseService;

    public PhaseController(PhaseService phaseService) {
        this.phaseService = phaseService;
    }

    @PostMapping("/api/projets/{projetId}/phases")
    @ResponseStatus(HttpStatus.CREATED)
    public PhaseResponseDto create(@PathVariable Long projetId,
                                   @Valid @RequestBody PhaseRequestDto requestDto) {
        return phaseService.create(projetId, requestDto);
    }

    @GetMapping("/api/projets/{projetId}/phases")
    public List<PhaseResponseDto> getByProjetId(@PathVariable Long projetId) {
        return phaseService.getByProjetId(projetId);
    }

    @GetMapping("/api/phases/{id}")
    public PhaseResponseDto getById(@PathVariable Long id) {
        return phaseService.getById(id);
    }

    @PutMapping("/api/phases/{id}")
    public PhaseResponseDto update(@PathVariable Long id,
                                   @Valid @RequestBody PhaseRequestDto requestDto) {
        return phaseService.update(id, requestDto);
    }

    @DeleteMapping("/api/phases/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        phaseService.delete(id);
    }

    @PatchMapping("/api/phases/{id}/realisation")
    public PhaseResponseDto updateEtatRealisation(@PathVariable Long id,
                                                  @Valid @RequestBody PhaseStatusUpdateDto dto) {
        return phaseService.updateEtatRealisation(id, dto.getValue());
    }

    @PatchMapping("/api/phases/{id}/facturation")
    public PhaseResponseDto updateEtatFacturation(@PathVariable Long id,
                                                  @Valid @RequestBody PhaseStatusUpdateDto dto) {
        return phaseService.updateEtatFacturation(id, dto.getValue());
    }

    @PatchMapping("/api/phases/{id}/paiement")
    public PhaseResponseDto updateEtatPaiement(@PathVariable Long id,
                                               @Valid @RequestBody PhaseStatusUpdateDto dto) {
        return phaseService.updateEtatPaiement(id, dto.getValue());
    }
}