package ma.toubkalit.controllers.reporting;


import lombok.RequiredArgsConstructor;
import ma.toubkalit.entity.facturation.Facture;
import ma.toubkalit.entity.projet.Phase;
import ma.toubkalit.services.reporting.ReportingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reporting")
@RequiredArgsConstructor
public class ReportingController {

    private final ReportingService reportingService;

    @GetMapping("/phases/facturees")
    public ResponseEntity<List<Phase>> getPhasesFacturees() {
        List<Phase> phases = reportingService.getPhasesFacturees();
        return ResponseEntity.ok(phases);
    }

    @GetMapping("/phases/payees")
    public ResponseEntity<List<Phase>> getPhasesPayees() {
        List<Phase> phases = reportingService.getPhasesPayees();
        return ResponseEntity.ok(phases);
    }

    @GetMapping("/factures")
    public ResponseEntity<List<Facture>> getAllFactures() {
        List<Facture> factures = reportingService.getAllFactures();
        return ResponseEntity.ok(factures);
    }
}