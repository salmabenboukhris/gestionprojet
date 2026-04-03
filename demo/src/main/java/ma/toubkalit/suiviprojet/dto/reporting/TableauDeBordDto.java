package ma.toubkalit.suiviprojet.dto.reporting;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableauDeBordDto {

    private long nombreProjets;
    private long nombreProjetsEnCours;
    private long nombreProjetsClotures;

    private long nombrePhases;
    private long nombrePhasesTermineesNonFacturees;
    private long nombrePhasesFactureesNonPayees;
    private long nombrePhasesPayees;

    private BigDecimal montantTotalProjets;
    private BigDecimal montantTotalPhases;
}