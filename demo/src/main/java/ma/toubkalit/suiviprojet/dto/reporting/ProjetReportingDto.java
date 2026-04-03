package ma.toubkalit.suiviprojet.dto.reporting;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjetReportingDto {

    private Long projetId;
    private String projetCode;
    private String projetNom;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private BigDecimal montant;

    private Long organismeId;
    private String organismeNom;

    private Long chefProjetId;
    private String chefProjetNomComplet;
}