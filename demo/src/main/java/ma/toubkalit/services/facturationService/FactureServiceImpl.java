package ma.toubkalit.services.facturationService;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.entity.facturation.Facture;
import ma.toubkalit.entity.projet.Phase;
import ma.toubkalit.enums.EtatFacturation;
import ma.toubkalit.enums.EtatRealisation;
import ma.toubkalit.repositories.facturationRepo.FactureRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FactureServiceImpl implements FactureService {

    private final FactureRepo factureRepo;

    @Override
    public Facture saveFacture(Facture facture) {
        if (facture.getCode() != null && factureRepo.findByCode(facture.getCode()).isPresent()) {
            throw new RuntimeException("Une facture avec ce code existe déjà.");
        }

        if (facture.getPhase() != null && facture.getPhase().getId() != null) {
            // La logique de vérification de phase terminée sera raffinée en Phase 11
            // Pour l'instant on garde une version simple
        }

        return factureRepo.save(facture);
    }

    @Override
    public Facture updateFacture(Integer id, Facture facture) {
        Facture existing = getFactureById(id);

        factureRepo.findByCode(facture.getCode())
                .ifPresent(f -> {
                    if (f.getId() != id) {
                        throw new RuntimeException("Une facture avec ce code existe deja.");
                    }
                });

        existing.setCode(facture.getCode());
        existing.setDateFacture(facture.getDateFacture());
        existing.setPhase(facture.getPhase());

        return factureRepo.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public Facture getFactureById(Integer id) {
        return factureRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Facture introuvable avec l'id : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Facture getFactureByCode(String code) {
        return factureRepo.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Facture introuvable avec le code : " + code));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Facture> getAllFactures() {
        return factureRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Facture getFactureByPhaseId(Integer phaseId) {
        return factureRepo.findByPhaseId(phaseId)
                .orElseThrow(() -> new RuntimeException("Aucune facture trouvée pour la phase id : " + phaseId));
    }

    @Override
    public void deleteFacture(Integer id) {
        Facture facture = getFactureById(id);
        factureRepo.delete(facture);
    }
}