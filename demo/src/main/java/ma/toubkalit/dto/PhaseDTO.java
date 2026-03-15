package ma.toubkalit.dto;

import lombok.*;
import ma.toubkalit.enums.EtatRealisation;
import ma.toubkalit.enums.EtatFacturation;
import ma.toubkalit.enums.EtatPaiement;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhaseDTO {
    private Integer id;
    private String code;
    private String libelle;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private double montant;
    private EtatRealisation etatRealisation;
    private EtatFacturation etatFacturation;
    private EtatPaiement etatPaiement;
    private Integer projetId;
}
