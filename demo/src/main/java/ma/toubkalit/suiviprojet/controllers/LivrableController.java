package ma.toubkalit.suiviprojet.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ma.toubkalit.suiviprojet.dto.livrable.LivrableRequestDto;
import ma.toubkalit.suiviprojet.dto.livrable.LivrableResponseDto;
import ma.toubkalit.suiviprojet.services.LivrableService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Livrables", description = "Gestion des livrables de phases (ADMIN, CHEF_PROJET)")
public class LivrableController {

    private final LivrableService livrableService;

    public LivrableController(LivrableService livrableService) {
        this.livrableService = livrableService;
    }

    @PostMapping("/api/phases/{phaseId}/livrables")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer un livrable pour une phase [ADMIN, CHEF_PROJET]")
    public LivrableResponseDto create(@PathVariable("phaseId") Long phaseId,
                                      @Valid @RequestBody LivrableRequestDto requestDto) {

        return livrableService.create(phaseId, requestDto);
    }

    @GetMapping("/api/phases/{phaseId}/livrables")
    @Operation(summary = "Lister les livrables d'une phase [ADMIN, CHEF_PROJET]")
    public List<LivrableResponseDto> getByPhaseId(@PathVariable("phaseId") Long phaseId) {

        return livrableService.getByPhaseId(phaseId);
    }

    @GetMapping("/api/livrables/{id}")
    @Operation(summary = "Récupérer un livrable par ID [ADMIN, CHEF_PROJET]")
    public LivrableResponseDto getById(@PathVariable("id") Long id) {

        return livrableService.getById(id);
    }

    @PutMapping("/api/livrables/{id}")
    @Operation(summary = "Mettre à jour un livrable [ADMIN, CHEF_PROJET]")
    public LivrableResponseDto update(@PathVariable("id") Long id,
                                      @Valid @RequestBody LivrableRequestDto requestDto) {

        return livrableService.update(id, requestDto);
    }

    @DeleteMapping("/api/livrables/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un livrable [ADMIN, CHEF_PROJET]")
    public void delete(@PathVariable("id") Long id) {

        livrableService.delete(id);
    }
}