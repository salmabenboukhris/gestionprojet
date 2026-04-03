package ma.toubkalit.suiviprojet.services.impl;

import ma.toubkalit.suiviprojet.dto.reporting.PhaseReportingDto;
import ma.toubkalit.suiviprojet.dto.reporting.ProjetReportingDto;
import ma.toubkalit.suiviprojet.dto.reporting.TableauDeBordDto;
import ma.toubkalit.suiviprojet.entities.Phase;
import ma.toubkalit.suiviprojet.entities.Projet;
import ma.toubkalit.suiviprojet.repositories.PhaseRepository;
import ma.toubkalit.suiviprojet.repositories.ProjetRepository;
import ma.toubkalit.suiviprojet.services.ReportingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReportingServiceImpl implements ReportingService {

    private final PhaseRepository phaseRepository;
    private final ProjetRepository projetRepository;

    public ReportingServiceImpl(PhaseRepository phaseRepository, ProjetRepository projetRepository) {
        this.phaseRepository = phaseRepository;
        this.projetRepository = projetRepository;
    }

    @Override
    public List<PhaseReportingDto> getPhasesTermineesNonFacturees(LocalDate dateDebut, LocalDate dateFin, Long projetId, Long chefProjetId) {
        return phaseRepository.findByEtatRealisationTrueAndEtatFacturationFalse().stream()
                .filter(p -> filterPhase(p, dateDebut, dateFin, projetId, chefProjetId))
                .map(this::mapPhase)
                .toList();
    }

    @Override
    public List<PhaseReportingDto> getPhasesFactureesNonPayees(LocalDate dateDebut, LocalDate dateFin, Long projetId, Long chefProjetId) {
        return phaseRepository.findByEtatFacturationTrueAndEtatPaiementFalse().stream()
                .filter(p -> filterPhase(p, dateDebut, dateFin, projetId, chefProjetId))
                .map(this::mapPhase)
                .toList();
    }

    @Override
    public List<PhaseReportingDto> getPhasesPayees(LocalDate dateDebut, LocalDate dateFin, Long projetId, Long chefProjetId) {
        return phaseRepository.findByEtatPaiementTrue().stream()
                .filter(p -> filterPhase(p, dateDebut, dateFin, projetId, chefProjetId))
                .map(this::mapPhase)
                .toList();
    }

    @Override
    public TableauDeBordDto getTableauDeBord() {
        List<Projet> projets = projetRepository.findAll();
        List<Phase> phases = phaseRepository.findAll();

        long projetsEnCours = projetRepository.findByDateFinGreaterThanEqual(LocalDate.now()).size();
        long projetsClotures = projetRepository.findByDateFinBefore(LocalDate.now()).size();

        long phasesTermineesNonFacturees = phaseRepository.findByEtatRealisationTrueAndEtatFacturationFalse().size();
        long phasesFactureesNonPayees = phaseRepository.findByEtatFacturationTrueAndEtatPaiementFalse().size();
        long phasesPayees = phaseRepository.findByEtatPaiementTrue().size();

        BigDecimal montantTotalProjets = projets.stream()
                .map(Projet::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal montantTotalPhases = phases.stream()
                .map(Phase::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return TableauDeBordDto.builder()
                .nombreProjets(projets.size())
                .nombreProjetsEnCours(projetsEnCours)
                .nombreProjetsClotures(projetsClotures)
                .nombrePhases(phases.size())
                .nombrePhasesTermineesNonFacturees(phasesTermineesNonFacturees)
                .nombrePhasesFactureesNonPayees(phasesFactureesNonPayees)
                .nombrePhasesPayees(phasesPayees)
                .montantTotalProjets(montantTotalProjets)
                .montantTotalPhases(montantTotalPhases)
                .build();
    }

    @Override
    public List<ProjetReportingDto> getProjetsEnCours() {
        return projetRepository.findByDateFinGreaterThanEqual(LocalDate.now()).stream()
                .map(this::mapProjet)
                .toList();
    }

    @Override
    public List<ProjetReportingDto> getProjetsClotures() {
        return projetRepository.findByDateFinBefore(LocalDate.now()).stream()
                .map(this::mapProjet)
                .toList();
    }

    private boolean filterPhase(Phase phase, LocalDate dateDebut, LocalDate dateFin, Long projetId, Long chefProjetId) {
        boolean ok = true;

        if (dateDebut != null) {
            ok = ok && !phase.getDateDebut().isBefore(dateDebut);
        }

        if (dateFin != null) {
            ok = ok && !phase.getDateFin().isAfter(dateFin);
        }

        if (projetId != null) {
            ok = ok && phase.getProjet().getId().equals(projetId);
        }

        if (chefProjetId != null) {
            ok = ok && phase.getProjet().getChefProjet().getId().equals(chefProjetId);
        }

        return ok;
    }

    private PhaseReportingDto mapPhase(Phase phase) {
        return PhaseReportingDto.builder()
                .phaseId(phase.getId())
                .phaseCode(phase.getCode())
                .phaseLibelle(phase.getLibelle())
                .dateDebut(phase.getDateDebut())
                .dateFin(phase.getDateFin())
                .montant(phase.getMontant())
                .etatRealisation(phase.getEtatRealisation())
                .etatFacturation(phase.getEtatFacturation())
                .etatPaiement(phase.getEtatPaiement())
                .projetId(phase.getProjet().getId())
                .projetCode(phase.getProjet().getCode())
                .projetNom(phase.getProjet().getNom())
                .chefProjetId(phase.getProjet().getChefProjet().getId())
                .chefProjetNomComplet(phase.getProjet().getChefProjet().getNom() + " " + phase.getProjet().getChefProjet().getPrenom())
                .build();
    }

    private ProjetReportingDto mapProjet(Projet projet) {
        return ProjetReportingDto.builder()
                .projetId(projet.getId())
                .projetCode(projet.getCode())
                .projetNom(projet.getNom())
                .description(projet.getDescription())
                .dateDebut(projet.getDateDebut())
                .dateFin(projet.getDateFin())
                .montant(projet.getMontant())
                .organismeId(projet.getOrganisme().getId())
                .organismeNom(projet.getOrganisme().getNom())
                .chefProjetId(projet.getChefProjet().getId())
                .chefProjetNomComplet(projet.getChefProjet().getNom() + " " + projet.getChefProjet().getPrenom())
                .build();
    }
}