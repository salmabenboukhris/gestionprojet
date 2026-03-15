package ma.toubkalit.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FactureDTO {
    private Integer id;
    private String code;
    private LocalDate dateFacture;
    private double montant;
    private String description;
    private Integer phaseId;
}
