package ma.toubkalit.services.projetService;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.entity.projet.Livrable;
import ma.toubkalit.repositories.projetRepo.LivrableRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LivrableServiceImpl implements LivrableService {

    private final LivrableRepo livrableRepo;

    @Override
    public Livrable saveLivrable(Livrable livrable) {
        return livrableRepo.save(livrable);
    }

    @Override
    public Livrable updateLivrable(Integer id, Livrable livrable) {
        Livrable existing = getLivrableById(id);
        existing.setCode(livrable.getCode());
        existing.setLibelle(livrable.getLibelle());
        existing.setDescription(livrable.getDescription());
        existing.setChemin(livrable.getChemin());
        existing.setPhase(livrable.getPhase());
        return livrableRepo.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public Livrable getLivrableById(Integer id) {
        return livrableRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Livrable introuvable avec l'id : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Livrable> getAllLivrables() {
        return livrableRepo.findAll();
    }

    @Override
    public void deleteLivrable(Integer id) {
        Livrable livrable = getLivrableById(id);
        livrableRepo.delete(livrable);
    }
}