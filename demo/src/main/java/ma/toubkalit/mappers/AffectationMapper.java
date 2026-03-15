package ma.toubkalit.mappers;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.dto.AffectationDTO;
import ma.toubkalit.entity.projet.Affectation;
import ma.toubkalit.entity.projet.AffectationId;
import ma.toubkalit.repositories.organisationRepo.EmployeRepo;
import ma.toubkalit.repositories.projetRepo.PhaseRepo;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AffectationMapper {

    private final EmployeRepo employeRepo;
    private final PhaseRepo phaseRepo;

    public AffectationDTO toDTO(Affectation entity) {
        if (entity == null) return null;
        return AffectationDTO.builder()
                .employeId(entity.getId().getEmployeId())
                .employeNom(entity.getEmploye() != null ? entity.getEmploye().getNom() + " " + entity.getEmploye().getPrenom() : null)
                .phaseId(entity.getId().getPhaseId())
                .phaseLibelle(entity.getPhase() != null ? entity.getPhase().getLibelle() : null)
                .dateDebut(entity.getDateDebut())
                .dateFin(entity.getDateFin())
                .build();
    }

    public Affectation toEntity(AffectationDTO dto) {
        if (dto == null) return null;
        AffectationId id = new AffectationId(dto.getEmployeId(), dto.getPhaseId());
        Affectation affectation = Affectation.builder()
                .id(id)
                .dateDebut(dto.getDateDebut())
                .dateFin(dto.getDateFin())
                .build();

        employeRepo.findById(dto.getEmployeId()).ifPresent(affectation::setEmploye);
        phaseRepo.findById(dto.getPhaseId()).ifPresent(affectation::setPhase);

        return affectation;
    }
}
