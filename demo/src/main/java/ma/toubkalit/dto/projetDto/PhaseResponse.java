package ma.toubkalit.dto.projetDto;


import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class PhaseResponse {
    private Integer id;
    private String code;
    private String libelle;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Double montant;
    private String etatRealisation;
    private String etatFacturation;
    private String etatPaiement;

    // Informations du projet
    private Integer projetId;
    private String projetCode;
    private String projetNom;

    // Statistiques
    private Integer nombreLivrables;
    private Integer nombreAffectations;
}
