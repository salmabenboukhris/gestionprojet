package ma.toubkalit.services.projetService;

import ma.toubkalit.entity.projet.Organisme;

import java.util.List;

public interface OrganismeService {

    Organisme saveOrganisme(Organisme organisme);

    Organisme updateOrganisme(Integer id, Organisme organisme);

    Organisme getOrganismeById(Integer id);

    Organisme getOrganismeByCode(String code);

    Organisme getOrganismeByNom(String nom);

    List<Organisme> getAllOrganismes();

    void deleteOrganisme(Integer id);
}