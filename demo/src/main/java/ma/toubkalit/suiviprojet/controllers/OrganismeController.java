package ma.toubkalit.suiviprojet.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ma.toubkalit.suiviprojet.dto.organisme.OrganismeRequestDto;
import ma.toubkalit.suiviprojet.dto.organisme.OrganismeResponseDto;
import ma.toubkalit.suiviprojet.services.OrganismeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organismes")
@Tag(name = "Organismes", description = "Gestion des organismes clients (ADMIN, SECRETAIRE)")
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
    public OrganismeResponseDto update(@PathVariable("id") Long id,
                                       @Valid @RequestBody OrganismeRequestDto requestDto) {

        return organismeService.update(id, requestDto);
    }

    @GetMapping("/{id}")
    public OrganismeResponseDto getById(@PathVariable("id") Long id) {

        return organismeService.getById(id);
    }

    @GetMapping
    public List<OrganismeResponseDto> getAll(
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "nom", required = false) String nom,
            @RequestParam(name = "contact", required = false) String contact
    ) {

        return organismeService.getAll(code, nom, contact);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {

        organismeService.delete(id);
    }
}