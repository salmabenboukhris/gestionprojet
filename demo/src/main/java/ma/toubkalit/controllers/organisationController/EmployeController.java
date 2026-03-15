package ma.toubkalit.controllers.organisationController;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.dto.EmployeDTO;
import ma.toubkalit.mappers.EmployeMapper;
import ma.toubkalit.services.organisationService.EmployeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employes")
@RequiredArgsConstructor
public class EmployeController {

    private final EmployeService employeService;
    private final EmployeMapper employeMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeDTO> create(@RequestBody EmployeDTO dto) {
        return new ResponseEntity<>(
                employeMapper.toDTO(employeService.saveEmploye(employeMapper.toEntity(dto))),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(employeMapper.toDTO(employeService.getEmployeById(id)));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<EmployeDTO> getAll() {
        return employeService.getAllEmployes().stream()
                .map(employeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/disponibles")
    @PreAuthorize("hasRole('CHEF_PROJET')")
    public List<EmployeDTO> getDisponibles(@RequestParam(required = false) String dateDebut,
                                         @RequestParam(required = false) String dateFin) {
        // Logique à implémenter en Phase 5 - Pour l'instant retourne tous
        return employeService.getAllEmployes().stream()
                .map(employeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeDTO> update(@PathVariable Integer id, @RequestBody EmployeDTO dto) {
        return ResponseEntity.ok(
                employeMapper.toDTO(employeService.updateEmploye(id, employeMapper.toEntity(dto)))
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        employeService.deleteEmploye(id);
        return ResponseEntity.noContent().build();
    }
}