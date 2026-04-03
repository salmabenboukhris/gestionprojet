package ma.toubkalit.suiviprojet.controllers;

import ma.toubkalit.suiviprojet.dto.reporting.PhaseReportingDto;
import ma.toubkalit.suiviprojet.dto.reporting.ProjetReportingDto;
import ma.toubkalit.suiviprojet.dto.reporting.TableauDeBordDto;
import ma.toubkalit.suiviprojet.services.ReportingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reporting")
public class ReportingController {

    private final ReportingService reportingService;

    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    @GetMapping("/phases/terminees-non-facturees")
    public List<PhaseReportingDto> getPhasesTermineesNonFacturees(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @RequestParam(required = false) Long projetId,
            @RequestParam(required = false) Long chefProjetId
    ) {
        return reportingService.getPhasesTermineesNonFacturees(dateDebut, dateFin, projetId, chefProjetId);
    }

    @GetMapping("/phases/facturees-non-payees")
    public List<PhaseReportingDto> getPhasesFactureesNonPayees(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @RequestParam(required = false) Long projetId,
            @RequestParam(required = false) Long chefProjetId
    ) {
        return reportingService.getPhasesFactureesNonPayees(dateDebut, dateFin, projetId, chefProjetId);
    }

    @GetMapping("/phases/payees")
    public List<PhaseReportingDto> getPhasesPayees(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @RequestParam(required = false) Long projetId,
            @RequestParam(required = false) Long chefProjetId
    ) {
        return reportingService.getPhasesPayees(dateDebut, dateFin, projetId, chefProjetId);
    }

    @GetMapping("/tableau-de-bord")
    public TableauDeBordDto getTableauDeBord() {
        return reportingService.getTableauDeBord();
    }

    @GetMapping("/projets/en-cours")
    public List<ProjetReportingDto> getProjetsEnCours() {
        return reportingService.getProjetsEnCours();
    }

    @GetMapping("/projets/clotures")
    public List<ProjetReportingDto> getProjetsClotures() {
        return reportingService.getProjetsClotures();
    }
}