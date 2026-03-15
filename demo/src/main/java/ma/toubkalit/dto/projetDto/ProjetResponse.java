package ma.toubkalit.dto.projetDto;


import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class ProjetResponse {
    private Integer id;
    private String code;
    private String nom;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Double montant;

    // Informations organisme
    private Integer organismeId;
    private String organismeNom;

    // Informations chef de projet
    private Integer chefProjetId;
    private String chefProjetNom;
    private String chefProjetPrenom;

    // Statistiques
    private Integer nombrePhases;
    private Double montantTotalPhases;
}