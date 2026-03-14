package ma.toubkalit.services.facturationService;

import ma.toubkalit.entity.facturation.Facture;

import java.util.List;

public interface FactureService {

    Facture saveFacture(Facture facture);

    Facture updateFacture(Integer id, Facture facture);

    Facture getFactureById(Integer id);

    Facture getFactureByCode(String code);

    List<Facture> getAllFactures();

    void deleteFacture(Integer id);
}