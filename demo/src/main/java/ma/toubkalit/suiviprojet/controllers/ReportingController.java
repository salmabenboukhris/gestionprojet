package ma.toubkalit.suiviprojet.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Reporting", description = "Statistiques et tableaux de bord (ADMIN, DIRECTEUR)")
public class ReportingController {

    private final ReportingService reportingService;

    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    @GetMapping("/phases/terminees-non-facturees")
    @Operation(summary = "Lister les phases terminées non facturées [ADMIN, DIRECTEUR]")
    public List<PhaseReportingDto> getPhasesTermineesNonFacturees(
            @RequestParam(name = "dateDebut", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(name = "dateFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @RequestParam(name = "projetId", required = false) Long projetId,
            @RequestParam(name = "chefProjetId", required = false) Long chefProjetId
    ) {

        return reportingService.getPhasesTermineesNonFacturees(dateDebut, dateFin, projetId, chefProjetId);
    }

    @GetMapping("/phases/facturees-non-payees")
    @Operation(summary = "Lister les phases facturées non payées [ADMIN, DIRECTEUR]")
    public List<PhaseReportingDto> getPhasesFactureesNonPayees(
            @RequestParam(name = "dateDebut", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(name = "dateFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @RequestParam(name = "projetId", required = false) Long projetId,
            @RequestParam(name = "chefProjetId", required = false) Long chefProjetId
    ) {

        return reportingService.getPhasesFactureesNonPayees(dateDebut, dateFin, projetId, chefProjetId);
    }

    @GetMapping("/phases/payees")
    @Operation(summary = "Lister les phases payées [ADMIN, DIRECTEUR]")
    public List<PhaseReportingDto> getPhasesPayees(
            @RequestParam(name = "dateDebut", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(name = "dateFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @RequestParam(name = "projetId", required = false) Long projetId,
            @RequestParam(name = "chefProjetId", required = false) Long chefProjetId
    ) {

        return reportingService.getPhasesPayees(dateDebut, dateFin, projetId, chefProjetId);
    }

    @GetMapping("/tableau-de-bord")
    @Operation(summary = "Récupérer le tableau de bord global [ADMIN, DIRECTEUR]")
    public TableauDeBordDto getTableauDeBord() {
        return reportingService.getTableauDeBord();
    }

    @GetMapping("/projets/en-cours")
    @Operation(summary = "Lister les projets en cours [ADMIN, DIRECTEUR]")
    public List<ProjetReportingDto> getProjetsEnCours() {
        return reportingService.getProjetsEnCours();
    }

    @GetMapping("/projets/clotures")
    @Operation(summary = "Lister les projets clôturés [ADMIN, DIRECTEUR]")
    public List<ProjetReportingDto> getProjetsClotures() {
        return reportingService.getProjetsClotures();
    }
}