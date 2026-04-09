package ma.toubkalit.suiviprojet.dto.facture;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FactureResponseDto {

    private Long id;
    private String code;
    private LocalDate dateFacture;
    private BigDecimal montant;
    private String statut;

    private Long phaseId;
    private String phaseCode;
    private String phaseLibelle;
    private Boolean etatRealisation;
    private Boolean etatFacturation;
    private Boolean etatPaiement;
}