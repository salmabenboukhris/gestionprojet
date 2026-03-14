package ma.toubkalit.services.projetService;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.entity.projet.Projet;
import ma.toubkalit.repositories.projetRepo.ProjetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjetServiceImpl implements ProjetService {

    private final ProjetRepository projetRepository;

    @Override
    public Projet saveProjet(Projet projet) {
        if (projetRepository.findByCode(projet.getCode()).isPresent()) {
            throw new RuntimeException("Un projet avec ce code existe deja.");
        }
        if (projet.getDateFin().isBefore(projet.getDateDebut())) {
            throw new RuntimeException("La date de fin doit etre superieure ou egale a la date de debut.");
        }
        return projetRepository.save(projet);
    }

    @Override
    public Projet updateProjet(Integer id, Projet projet) {
        Projet existing = getProjetById(id);

        projetRepository.findByCode(projet.getCode())
                .ifPresent(p -> {
                    if (p.getId() != id) {
                        throw new RuntimeException("Un projet avec ce code existe deja.");
                    }
                });

        if (projet.getDateFin().isBefore(projet.getDateDebut())) {
            throw new RuntimeException("La date de fin doit etre superieure ou egale a la date de debut.");
        }

        existing.setCode(projet.getCode());
        existing.setNom(projet.getNom());
        existing.setDescription(projet.getDescription());
        existing.setDateDebut(projet.getDateDebut());
        existing.setDateFin(projet.getDateFin());
        existing.setMontant(projet.getMontant());
        existing.setOrganisme(projet.getOrganisme());
        existing.setChefProjet(projet.getChefProjet());

        return projetRepository.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public Projet getProjetById(Integer id) {
        return projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet introuvable avec l'id : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Projet getProjetByCode(String code) {
        return projetRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Projet introuvable avec le code : " + code));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Projet> getAllProjets() {
        return projetRepository.findAll();
    }

    @Override
    public void deleteProjet(Integer id) {
        Projet projet = getProjetById(id);
        projetRepository.delete(projet);
    }
}