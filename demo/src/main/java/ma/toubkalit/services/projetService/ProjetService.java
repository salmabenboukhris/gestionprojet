package ma.toubkalit.services.projetService;

import ma.toubkalit.entity.projet.Projet;

import java.util.List;

public interface ProjetService {

    Projet saveProjet(Projet projet);

    Projet updateProjet(Integer id, Projet projet);

    Projet getProjetById(Integer id);

    Projet getProjetByCode(String code);

    List<Projet> getAllProjets();

    void deleteProjet(Integer id);
}