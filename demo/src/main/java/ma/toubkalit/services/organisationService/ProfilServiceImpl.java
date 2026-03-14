package ma.toubkalit.services.organisationService;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.entity.organisation.Profil;
import ma.toubkalit.repositories.organisationRepo.ProfilRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfilServiceImpl implements ProfilService {

    private final ProfilRepo profilRepo;

    @Override
    public Profil saveProfil(Profil profil) {
        return profilRepo.save(profil);
    }

    @Override
    public Profil updateProfil(Integer id, Profil profil) {
        Profil existing = getProfilById(id);
        existing.setCode(profil.getCode());
        existing.setLibelle(profil.getLibelle());
        return profilRepo.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public Profil getProfilById(Integer id) {
        return profilRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Profil introuvable avec l'id : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Profil> getAllProfils() {
        return profilRepo.findAll();
    }

    @Override
    public void deleteProfil(Integer id) {
        Profil profil = getProfilById(id);
        profilRepo.delete(profil);
    }
}