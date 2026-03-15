package ma.toubkalit.services.reporting;

import ma.toubkalit.dto.DashboardDTO;
import ma.toubkalit.entity.facturation.Facture;
import ma.toubkalit.entity.projet.Phase;
import ma.toubkalit.entity.projet.Projet;

import java.util.List;

public interface ReportingService {

    List<Phase> getPhasesTermineesNonFacturees();

    List<Phase> getPhasesFactureesNonPayees();

    List<Phase> getPhasesPayees();

    List<Projet> getProjetsEnCours();

    DashboardDTO getDashboardStats();

    List<Facture> getAllFactures();
}