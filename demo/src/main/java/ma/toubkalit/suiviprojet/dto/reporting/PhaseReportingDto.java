package ma.toubkalit.suiviprojet.dto.reporting;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhaseReportingDto {

    private Long phaseId;
    private String phaseCode;
    private String phaseLibelle;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private BigDecimal montant;
    private Boolean etatRealisation;
    private Boolean etatFacturation;
    private Boolean etatPaiement;

    private Long projetId;
    private String projetCode;
    private String projetNom;

    private Long chefProjetId;
    private String chefProjetNomComplet;
}