package ma.toubkalit.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjetDTO {
    private Integer id;
    private String code;
    private String nom;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private double montant;
    private Integer organismeId;
    private String organismeNom;
    private Integer chefProjetId;
    private String chefProjetNom;
}
