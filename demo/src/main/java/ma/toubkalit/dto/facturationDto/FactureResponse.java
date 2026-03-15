package ma.toubkalit.dto.facturationDto;


import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class FactureResponse {
    private Integer id;
    private String code;
    private LocalDate dateFacture;

    // Informations de la phase associée
    private Integer phaseId;
    private String phaseCode;
    private String phaseLibelle;
    private Double montantPhase;

    // Informations du projet
    private Integer projetId;
    private String projetCode;
    private String projetNom;
    private String organismeNom;
}
