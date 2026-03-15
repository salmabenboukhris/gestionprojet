package ma.toubkalit.controllers.facturationController;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.dto.FactureDTO;
import ma.toubkalit.mappers.FactureMapper;
import ma.toubkalit.services.facturationService.FactureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@PreAuthorize("hasRole('COMPTABLE')")
public class FactureController {

    private final FactureService factureService;
    private final FactureMapper factureMapper;

    @PostMapping("/phases/{phaseId}/facture")
    public ResponseEntity<FactureDTO> create(@PathVariable Integer phaseId, @RequestBody FactureDTO dto) {
        dto.setPhaseId(phaseId);
        return new ResponseEntity<>(
                factureMapper.toDTO(factureService.saveFacture(factureMapper.toEntity(dto))),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/factures")
    public List<FactureDTO> getAll() {
        return factureService.getAllFactures().stream()
                .map(factureMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/factures/{id}")
    public ResponseEntity<FactureDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(factureMapper.toDTO(factureService.getFactureById(id)));
    }

    @PutMapping("/factures/{id}")
    public ResponseEntity<FactureDTO> update(@PathVariable Integer id, @RequestBody FactureDTO dto) {
        return ResponseEntity.ok(
                factureMapper.toDTO(factureService.updateFacture(id, factureMapper.toEntity(dto)))
        );
    }

    @DeleteMapping("/factures/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        factureService.deleteFacture(id);
        return ResponseEntity.noContent().build();
    }
}