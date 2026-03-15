package ma.toubkalit.controllers.reporting;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.dto.DashboardDTO;
import ma.toubkalit.dto.PhaseDTO;
import ma.toubkalit.dto.ProjetDTO;
import ma.toubkalit.mappers.PhaseMapper;
import ma.toubkalit.mappers.ProjetMapper;
import ma.toubkalit.services.reporting.ReportingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reporting")
@RequiredArgsConstructor
public class ReportingController {

    private final ReportingService reportingService;
    private final PhaseMapper phaseMapper;
    private final ProjetMapper projetMapper;

    @GetMapping("/phases/terminees-non-facturees")
    @PreAuthorize("hasRole('COMPTABLE')")
    public List<PhaseDTO> getPhasesTermineesNonFacturees() {
        return reportingService.getPhasesTermineesNonFacturees().stream()
                .map(phaseMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/phases/facturees-non-payees")
    @PreAuthorize("hasRole('COMPTABLE')")
    public List<PhaseDTO> getPhasesFactureesNonPayees() {
        return reportingService.getPhasesFactureesNonPayees().stream()
                .map(phaseMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/phases/payees")
    @PreAuthorize("hasRole('COMPTABLE')")
    public List<PhaseDTO> getPhasesPayees() {
        return reportingService.getPhasesPayees().stream()
                .map(phaseMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/tableau-de-bord")
    @PreAuthorize("hasRole('DIRECTEUR')")
    public DashboardDTO getTableauDeBord() {
        return reportingService.getDashboardStats();
    }

    @GetMapping("/projets/en-cours")
    @PreAuthorize("hasRole('DIRECTEUR')")
    public List<ProjetDTO> getProjetsEnCours() {
        return reportingService.getProjetsEnCours().stream()
                .map(projetMapper::toDTO)
                .collect(Collectors.toList());
    }
}