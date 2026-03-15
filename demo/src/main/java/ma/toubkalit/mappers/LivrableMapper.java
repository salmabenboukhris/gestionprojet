package ma.toubkalit.mappers;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.dto.LivrableDTO;
import ma.toubkalit.entity.projet.Livrable;
import ma.toubkalit.repositories.projetRepo.PhaseRepo;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LivrableMapper {

    private final PhaseRepo phaseRepo;

    public LivrableDTO toDTO(Livrable entity) {
        if (entity == null) return null;
        return LivrableDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .libelle(entity.getLibelle())
                .description(entity.getDescription())
                .chemin(entity.getChemin())
                .phaseId(entity.getPhase() != null ? entity.getPhase().getId() : null)
                .build();
    }

    public Livrable toEntity(LivrableDTO dto) {
        if (dto == null) return null;
        Livrable livrable = Livrable.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .libelle(dto.getLibelle())
                .description(dto.getDescription())
                .chemin(dto.getChemin())
                .build();

        if (dto.getPhaseId() != null) {
            phaseRepo.findById(dto.getPhaseId()).ifPresent(livrable::setPhase);
        }

        return livrable;
    }
}
