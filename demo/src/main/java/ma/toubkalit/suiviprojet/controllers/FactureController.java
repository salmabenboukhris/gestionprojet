package ma.toubkalit.suiviprojet.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ma.toubkalit.suiviprojet.dto.facture.FactureRequestDto;
import ma.toubkalit.suiviprojet.dto.facture.FactureResponseDto;
import ma.toubkalit.suiviprojet.services.FactureService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Factures", description = "Gestion de la facturation (ADMIN, COMPTABLE)")
public class FactureController {

    private final FactureService factureService;

    public FactureController(FactureService factureService) {
        this.factureService = factureService;
    }

    @PostMapping("/api/phases/{phaseId}/facture")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer une facture pour une phase terminée [ADMIN, COMPTABLE]")
    public FactureResponseDto create(@PathVariable("phaseId") Long phaseId,
                                     @Valid @RequestBody FactureRequestDto requestDto) {

        return factureService.create(phaseId, requestDto);
    }

    @GetMapping("/api/factures")
    @Operation(summary = "Lister toutes les factures [ADMIN, COMPTABLE]")
    public List<FactureResponseDto> getAll() {
        return factureService.getAll();
    }

    @GetMapping("/api/factures/{id}")
    @Operation(summary = "Récupérer une facture par ID [ADMIN, COMPTABLE]")
    public FactureResponseDto getById(@PathVariable("id") Long id) {

        return factureService.getById(id);
    }

    @PutMapping("/api/factures/{id}")
    @Operation(summary = "Mettre à jour une facture [ADMIN, COMPTABLE]")
    public FactureResponseDto update(@PathVariable("id") Long id,
                                     @Valid @RequestBody FactureRequestDto requestDto) {

        return factureService.update(id, requestDto);
    }

    @DeleteMapping("/api/factures/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer une facture [ADMIN, COMPTABLE]")
    public void delete(@PathVariable("id") Long id) {

        factureService.delete(id);
    }
}