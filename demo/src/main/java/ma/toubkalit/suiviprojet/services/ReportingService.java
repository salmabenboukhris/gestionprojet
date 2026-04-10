package ma.toubkalit.suiviprojet.services;

import ma.toubkalit.suiviprojet.dto.reporting.PhaseReportingDto;
import ma.toubkalit.suiviprojet.dto.reporting.ProjetReportingDto;
import ma.toubkalit.suiviprojet.dto.reporting.TableauDeBordDto;

import java.time.LocalDate;
import java.util.List;

public interface ReportingService {

    List<PhaseReportingDto> getPhasesTermineesNonFacturees(LocalDate dateDebut, LocalDate dateFin, Long projetId, Long chefProjetId);

    List<PhaseReportingDto> getPhasesFactureesNonPayees(LocalDate dateDebut, LocalDate dateFin, Long projetId, Long chefProjetId);

    List<PhaseReportingDto> getPhasesPayees(LocalDate dateDebut, LocalDate dateFin, Long projetId, Long chefProjetId);

    TableauDeBordDto getTableauDeBord();

    List<ProjetReportingDto> getProjetsEnCours();

    List<ProjetReportingDto> getProjetsClotures();
}