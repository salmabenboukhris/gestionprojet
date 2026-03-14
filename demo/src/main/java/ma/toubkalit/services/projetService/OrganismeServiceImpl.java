package ma.toubkalit.services.projetService;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.entity.projet.Organisme;
import ma.toubkalit.repositories.projetRepo.OrganismeRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrganismeServiceImpl implements OrganismeService {

    private final OrganismeRepo organismeRepo;

    @Override
    public Organisme saveOrganisme(Organisme organisme) {
        if (organismeRepo.findByCode(organisme.getCode()).isPresent()) {
            throw new RuntimeException("Un organisme avec ce code existe deja.");
        }
        return organismeRepo.save(organisme);
    }

    @Override
    public Organisme updateOrganisme(Integer id, Organisme organisme) {
        Organisme existing = getOrganismeById(id);

        organismeRepo.findByCode(organisme.getCode())
                .ifPresent(o -> {
                    if (o.getId() != id) {
                        throw new RuntimeException("Un organisme avec ce code existe deja.");
                    }
                });

        existing.setCode(organisme.getCode());
        existing.setNom(organisme.getNom());
        existing.setAdresse(organisme.getAdresse());
        existing.setTelephone(organisme.getTelephone());
        existing.setNomContact(organisme.getNomContact());
        existing.setEmailContact(organisme.getEmailContact());
        existing.setSiteWeb(organisme.getSiteWeb());

        return organismeRepo.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public Organisme getOrganismeById(Integer id) {
        return organismeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Organisme introuvable avec l'id : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Organisme getOrganismeByCode(String code) {
        return organismeRepo.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Organisme introuvable avec le code : " + code));
    }

    @Override
    @Transactional(readOnly = true)
    public Organisme getOrganismeByNom(String nom) {
        return organismeRepo.findByNom(nom)
                .orElseThrow(() -> new RuntimeException("Organisme introuvable avec le nom : " + nom));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Organisme> getAllOrganismes() {
        return organismeRepo.findAll();
    }

    @Override
    public void deleteOrganisme(Integer id) {
        Organisme organisme = getOrganismeById(id);
        organismeRepo.delete(organisme);
    }
}