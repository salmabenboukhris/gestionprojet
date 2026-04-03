package ma.toubkalit.suiviprojet.controllers;

import jakarta.validation.Valid;
import ma.toubkalit.suiviprojet.dto.facture.FactureRequestDto;
import ma.toubkalit.suiviprojet.dto.facture.FactureResponseDto;
import ma.toubkalit.suiviprojet.services.FactureService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FactureController {

    private final FactureService factureService;

    public FactureController(FactureService factureService) {
        this.factureService = factureService;
    }

    @PostMapping("/api/phases/{phaseId}/facture")
    @ResponseStatus(HttpStatus.CREATED)
    public FactureResponseDto create(@PathVariable Long phaseId,
                                     @Valid @RequestBody FactureRequestDto requestDto) {
        return factureService.create(phaseId, requestDto);
    }

    @GetMapping("/api/factures")
    public List<FactureResponseDto> getAll() {
        return factureService.getAll();
    }

    @GetMapping("/api/factures/{id}")
    public FactureResponseDto getById(@PathVariable Long id) {
        return factureService.getById(id);
    }

    @PutMapping("/api/factures/{id}")
    public FactureResponseDto update(@PathVariable Long id,
                                     @Valid @RequestBody FactureRequestDto requestDto) {
        return factureService.update(id, requestDto);
    }

    @DeleteMapping("/api/factures/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        factureService.delete(id);
    }
}