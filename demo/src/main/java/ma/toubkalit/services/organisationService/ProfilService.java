package ma.toubkalit.services.organisationService;

import ma.toubkalit.entity.organisation.Profil;

import java.util.List;

public interface ProfilService {

    Profil saveProfil(Profil profil);

    Profil updateProfil(Integer id, Profil profil);

    Profil getProfilById(Integer id);

    List<Profil> getAllProfils();

    void deleteProfil(Integer id);
}