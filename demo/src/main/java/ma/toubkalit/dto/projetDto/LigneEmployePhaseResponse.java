package ma.toubkalit.dto.projetDto;


import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@Builder
public class LigneEmployePhaseResponse {
    private Integer employeId;
    private Integer phaseId;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Double chargeHoraire;

    // Informations employé
    private String employeMatricule;
    private String employeNom;
    private String employePrenom;

    // Informations phase
    private String phaseCode;
    private String phaseLibelle;
    private String phaseEtat;
}