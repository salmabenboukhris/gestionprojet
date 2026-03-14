package ma.toubkalit.services.projetService;

import ma.toubkalit.entity.projet.Livrable;

import java.util.List;

public interface LivrableService {

    Livrable saveLivrable(Livrable livrable);

    Livrable updateLivrable(Integer id, Livrable livrable);

    Livrable getLivrableById(Integer id);

    List<Livrable> getAllLivrables();

    void deleteLivrable(Integer id);
}