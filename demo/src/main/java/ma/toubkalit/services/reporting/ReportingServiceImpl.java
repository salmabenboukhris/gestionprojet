package ma.toubkalit.services.reporting;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.dto.DashboardDTO;
import ma.toubkalit.entity.facturation.Facture;
import ma.toubkalit.entity.projet.Phase;
import ma.toubkalit.entity.projet.Projet;
import ma.toubkalit.enums.EtatFacturation;
import ma.toubkalit.enums.EtatPaiement;
import ma.toubkalit.enums.EtatRealisation;
import ma.toubkalit.repositories.facturationRepo.FactureRepo;
import ma.toubkalit.repositories.organisationRepo.EmployeRepo;
import ma.toubkalit.repositories.projetRepo.OrganismeRepo;
import ma.toubkalit.repositories.projetRepo.PhaseRepo;
import ma.toubkalit.repositories.projetRepo.ProjetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportingServiceImpl implements ReportingService {

    private final PhaseRepo phaseRepo;
    private final FactureRepo factureRepo;
    private final ProjetRepository projetRepo;
    private final EmployeRepo employeRepo;
    private final OrganismeRepo organismeRepo;

    @Override
    public List<Phase> getPhasesTermineesNonFacturees() {
        return phaseRepo.findAll().stream()
                .filter(p -> p.getEtatRealisation() == EtatRealisation.TERMINEE &&
                            p.getEtatFacturation() == EtatFacturation.NON_FACTUREE)
                .toList();
    }

    @Override
    public List<Phase> getPhasesFactureesNonPayees() {
        return phaseRepo.findAll().stream()
                .filter(p -> p.getEtatFacturation() == EtatFacturation.FACTUREE &&
                            p.getEtatPaiement() == EtatPaiement.NON_PAYEE)
                .toList();
    }

    @Override
    public List<Phase> getPhasesPayees() {
        return phaseRepo.findByEtatPaiement(EtatPaiement.PAYEE);
    }

    @Override
    public List<Projet> getProjetsEnCours() {
        return projetRepo.findAll();
    }

    @Override
    public DashboardDTO getDashboardStats() {
        return DashboardDTO.builder()
                .totalProjets(projetRepo.count())
                .projetsEnCours(projetRepo.count())
                .montantTotalProjets(projetRepo.findAll().stream().mapToDouble(Projet::getMontant).sum())
                .totalEmployes(employeRepo.count())
                .totalOrganismes(organismeRepo.count())
                .montantTotalFacture(factureRepo.findAll().stream().mapToDouble(Facture::getMontant).sum())
                .montantTotalPaye(phaseRepo.findByEtatPaiement(EtatPaiement.PAYEE).stream().mapToDouble(Phase::getMontant).sum())
                .build();
    }

    @Override
    public List<Facture> getAllFactures() {
        return factureRepo.findAll();
    }
}