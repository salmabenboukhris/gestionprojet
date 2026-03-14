package ma.toubkalit.services.reporting;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.entity.facturation.Facture;
import ma.toubkalit.entity.projet.Phase;
import ma.toubkalit.enums.EtatFacturation;
import ma.toubkalit.enums.EtatPaiement;
import ma.toubkalit.repositories.facturationRepo.FactureRepo;
import ma.toubkalit.repositories.projetRepo.PhaseRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportingServiceImpl implements ReportingService {

    private final PhaseRepo phaseRepo;
    private final FactureRepo factureRepo;

    @Override
    public List<Phase> getPhasesFacturees() {
        return phaseRepo.findByEtatFacturation(EtatFacturation.FACTUREE);
    }

    @Override
    public List<Phase> getPhasesPayees() {
        return phaseRepo.findByEtatPaiement(EtatPaiement.PAYEE);
    }

    @Override
    public List<Facture> getAllFactures() {
        return factureRepo.findAll();
    }
}