package ma.toubkalit.services.reporting;

import ma.toubkalit.entity.facturation.Facture;
import ma.toubkalit.entity.projet.Phase;

import java.util.List;

public interface ReportingService {

    List<Phase> getPhasesFacturees();

    List<Phase> getPhasesPayees();

    List<Facture> getAllFactures();
}