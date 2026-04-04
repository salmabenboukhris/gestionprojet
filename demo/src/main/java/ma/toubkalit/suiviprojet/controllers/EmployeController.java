package ma.toubkalit.suiviprojet.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ma.toubkalit.suiviprojet.dto.employe.EmployeRequestDto;
import ma.toubkalit.suiviprojet.dto.employe.EmployeResponseDto;
import ma.toubkalit.suiviprojet.dto.employe.EmployeSearchResponseDto;
import ma.toubkalit.suiviprojet.services.EmployeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/employes")
@Tag(name = "Employés", description = "Gestion des employés (Réservé à l'ADMIN)")
public class EmployeController {

    private final EmployeService employeService;

    public EmployeController(EmployeService employeService) {
        this.employeService = employeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer un nouvel employé [ADMIN]")
    public EmployeResponseDto create(@Valid @RequestBody EmployeRequestDto requestDto) {
        return employeService.create(requestDto);
    }

    @PutMapping("/{id}")
    public EmployeResponseDto update(@PathVariable("id") Long id,
                                     @Valid @RequestBody EmployeRequestDto requestDto) {

        return employeService.update(id, requestDto);
    }

    @GetMapping("/{id}")
    public EmployeResponseDto getById(@PathVariable("id") Long id) {

        return employeService.getById(id);
    }

    @GetMapping
    public List<EmployeSearchResponseDto> search(
            @RequestParam(name = "matricule", required = false) String matricule,
            @RequestParam(name = "login", required = false) String login,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "nom", required = false) String nom
    ) {

        return employeService.search(matricule, login, email, nom);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {

        employeService.delete(id);
    }

    @GetMapping("/disponibles")
    public List<EmployeSearchResponseDto> getDisponibles(
            @RequestParam("dateDebut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam("dateFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin
    ) {

        return employeService.getDisponibles(dateDebut, dateFin);
    }
}