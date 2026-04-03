package ma.toubkalit.suiviprojet.controllers;

import jakarta.validation.Valid;
import ma.toubkalit.suiviprojet.dto.livrable.LivrableRequestDto;
import ma.toubkalit.suiviprojet.dto.livrable.LivrableResponseDto;
import ma.toubkalit.suiviprojet.services.LivrableService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LivrableController {

    private final LivrableService livrableService;

    public LivrableController(LivrableService livrableService) {
        this.livrableService = livrableService;
    }

    @PostMapping("/api/phases/{phaseId}/livrables")
    @ResponseStatus(HttpStatus.CREATED)
    public LivrableResponseDto create(@PathVariable Long phaseId,
                                      @Valid @RequestBody LivrableRequestDto requestDto) {
        return livrableService.create(phaseId, requestDto);
    }

    @GetMapping("/api/phases/{phaseId}/livrables")
    public List<LivrableResponseDto> getByPhaseId(@PathVariable Long phaseId) {
        return livrableService.getByPhaseId(phaseId);
    }

    @GetMapping("/api/livrables/{id}")
    public LivrableResponseDto getById(@PathVariable Long id) {
        return livrableService.getById(id);
    }

    @PutMapping("/api/livrables/{id}")
    public LivrableResponseDto update(@PathVariable Long id,
                                      @Valid @RequestBody LivrableRequestDto requestDto) {
        return livrableService.update(id, requestDto);
    }

    @DeleteMapping("/api/livrables/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        livrableService.delete(id);
    }
}