package ma.toubkalit.controllers.projetController;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.dto.OrganismeDTO;
import ma.toubkalit.mappers.OrganismeMapper;
import ma.toubkalit.services.projetService.OrganismeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/organismes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SECRETAIRE')")
public class OrganismeController {

    private final OrganismeService organismeService;
    private final OrganismeMapper organismeMapper;

    @PostMapping
    public ResponseEntity<OrganismeDTO> create(@RequestBody OrganismeDTO dto) {
        return new ResponseEntity<>(
                organismeMapper.toDTO(organismeService.saveOrganisme(organismeMapper.toEntity(dto))),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganismeDTO> update(@PathVariable Integer id, @RequestBody OrganismeDTO dto) {
        return ResponseEntity.ok(
                organismeMapper.toDTO(organismeService.updateOrganisme(id, organismeMapper.toEntity(dto)))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganismeDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(organismeMapper.toDTO(organismeService.getOrganismeById(id)));
    }

    @GetMapping
    public List<OrganismeDTO> getAll() {
        return organismeService.getAllOrganismes().stream()
                .map(organismeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        organismeService.deleteOrganisme(id);
        return ResponseEntity.noContent().build();
    }
}