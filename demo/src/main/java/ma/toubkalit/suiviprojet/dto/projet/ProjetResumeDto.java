package ma.toubkalit.suiviprojet.dto.projet;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjetResumeDto {

    private Long id;
    private String code;
    private String nom;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private BigDecimal montant;

    private String organismeNom;
    private String chefProjetNomComplet;

    private int nombrePhases;
    private int nombreDocuments;
}