package ma.toubkalit.suiviprojet.controllers;

import jakarta.validation.Valid;
import ma.toubkalit.suiviprojet.dto.organisme.OrganismeRequestDto;
import ma.toubkalit.suiviprojet.dto.organisme.OrganismeResponseDto;
import ma.toubkalit.suiviprojet.services.OrganismeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organismes")
public class OrganismeController {

    private final OrganismeService organismeService;

    public OrganismeController(OrganismeService organismeService) {
        this.organismeService = organismeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrganismeResponseDto create(@Valid @RequestBody OrganismeRequestDto requestDto) {
        return organismeService.create(requestDto);
    }

    @PutMapping("/{id}")
    public OrganismeResponseDto update(@PathVariable Long id,
                                       @Valid @RequestBody OrganismeRequestDto requestDto) {
        return organismeService.update(id, requestDto);
    }

    @GetMapping("/{id}")
    public OrganismeResponseDto getById(@PathVariable Long id) {
        return organismeService.getById(id);
    }

    @GetMapping
    public List<OrganismeResponseDto> getAll(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String contact
    ) {
        return organismeService.getAll(code, nom, contact);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        organismeService.delete(id);
    }
}