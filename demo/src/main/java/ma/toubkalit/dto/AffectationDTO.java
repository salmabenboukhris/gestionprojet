package ma.toubkalit.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AffectationDTO {
    private Integer employeId;
    private String employeNom;
    private Integer phaseId;
    private String phaseLibelle;
    private LocalDate dateDebut;
    private LocalDate dateFin;
}
