package ma.toubkalit.suiviprojet.dto.phase;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhaseResponseDto {

    private Long id;
    private String code;
    private String libelle;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private BigDecimal montant;
    private Boolean etatRealisation;
    private Boolean etatFacturation;
    private Boolean etatPaiement;
    private Integer tauxRealisation;  // 0-100

    private Long projetId;
    private String projetCode;
    private String projetNom;
}