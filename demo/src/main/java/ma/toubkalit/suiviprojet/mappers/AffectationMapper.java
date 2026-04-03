package ma.toubkalit.suiviprojet.mappers;

import ma.toubkalit.suiviprojet.dto.affectation.AffectationRequestDto;
import ma.toubkalit.suiviprojet.dto.affectation.AffectationResponseDto;
import ma.toubkalit.suiviprojet.entities.Affectation;
import ma.toubkalit.suiviprojet.entities.AffectationId;
import ma.toubkalit.suiviprojet.entities.Employe;
import ma.toubkalit.suiviprojet.entities.Phase;
import org.springframework.stereotype.Component;

@Component
public class AffectationMapper {

    public Affectation toEntity(AffectationRequestDto dto, Employe employe, Phase phase) {
        return Affectation.builder()
                .id(new AffectationId(employe.getId(), phase.getId()))
                .employe(employe)
                .phase(phase)
                .dateDebut(dto.getDateDebut())
                .dateFin(dto.getDateFin())
                .build();
    }

    public void updateEntity(Affectation affectation, AffectationRequestDto dto) {
        affectation.setDateDebut(dto.getDateDebut());
        affectation.setDateFin(dto.getDateFin());
    }

    public AffectationResponseDto toResponseDto(Affectation affectation) {
        return AffectationResponseDto.builder()
                .employeId(affectation.getEmploye().getId())
                .employeMatricule(affectation.getEmploye().getMatricule())
                .employeNomComplet(affectation.getEmploye().getNom() + " " + affectation.getEmploye().getPrenom())
                .phaseId(affectation.getPhase().getId())
                .phaseCode(affectation.getPhase().getCode())
                .phaseLibelle(affectation.getPhase().getLibelle())
                .dateDebut(affectation.getDateDebut())
                .dateFin(affectation.getDateFin())
                .build();
    }
}