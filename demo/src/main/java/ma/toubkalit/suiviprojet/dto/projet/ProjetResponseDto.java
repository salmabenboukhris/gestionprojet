package ma.toubkalit.suiviprojet.dto.projet;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjetResponseDto {

    private Long id;
    private String code;
    private String nom;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private BigDecimal montant;

    private Long organismeId;
    private String organismeCode;
    private String organismeNom;

    private Long chefProjetId;
    private String chefProjetNomComplet;
}