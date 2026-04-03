package ma.toubkalit.suiviprojet.dto.affectation;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AffectationResponseDto {

    private Long employeId;
    private String employeMatricule;
    private String employeNomComplet;

    private Long phaseId;
    private String phaseCode;
    private String phaseLibelle;

    private LocalDate dateDebut;
    private LocalDate dateFin;
}