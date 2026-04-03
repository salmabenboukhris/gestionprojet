package ma.toubkalit.suiviprojet.controllers;

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
public class EmployeController {

    private final EmployeService employeService;

    public EmployeController(EmployeService employeService) {
        this.employeService = employeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeResponseDto create(@Valid @RequestBody EmployeRequestDto requestDto) {
        return employeService.create(requestDto);
    }

    @PutMapping("/{id}")
    public EmployeResponseDto update(@PathVariable Long id,
                                     @Valid @RequestBody EmployeRequestDto requestDto) {
        return employeService.update(id, requestDto);
    }

    @GetMapping("/{id}")
    public EmployeResponseDto getById(@PathVariable Long id) {
        return employeService.getById(id);
    }

    @GetMapping
    public List<EmployeSearchResponseDto> search(
            @RequestParam(required = false) String matricule,
            @RequestParam(required = false) String login,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String nom
    ) {
        return employeService.search(matricule, login, email, nom);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        employeService.delete(id);
    }

    @GetMapping("/disponibles")
    public List<EmployeSearchResponseDto> getDisponibles(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin
    ) {
        return employeService.getDisponibles(dateDebut, dateFin);
    }
}