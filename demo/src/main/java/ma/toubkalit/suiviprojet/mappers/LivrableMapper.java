package ma.toubkalit.suiviprojet.mappers;

import ma.toubkalit.suiviprojet.dto.livrable.LivrableRequestDto;
import ma.toubkalit.suiviprojet.dto.livrable.LivrableResponseDto;
import ma.toubkalit.suiviprojet.entities.Livrable;
import ma.toubkalit.suiviprojet.entities.Phase;
import org.springframework.stereotype.Component;

@Component
public class LivrableMapper {

    public Livrable toEntity(LivrableRequestDto dto, Phase phase) {
        return Livrable.builder()
                .code(dto.getCode())
                .libelle(dto.getLibelle())
                .description(dto.getDescription())
                .chemin(dto.getChemin())
                .phase(phase)
                .build();
    }

    public void updateEntity(Livrable livrable, LivrableRequestDto dto) {
        livrable.setCode(dto.getCode());
        livrable.setLibelle(dto.getLibelle());
        livrable.setDescription(dto.getDescription());
        livrable.setChemin(dto.getChemin());
    }

    public LivrableResponseDto toResponseDto(Livrable livrable) {
        return LivrableResponseDto.builder()
                .id(livrable.getId())
                .code(livrable.getCode())
                .libelle(livrable.getLibelle())
                .description(livrable.getDescription())
                .chemin(livrable.getChemin())
                .phaseId(livrable.getPhase() != null ? livrable.getPhase().getId() : null)
                .phaseCode(livrable.getPhase() != null ? livrable.getPhase().getCode() : null)
                .phaseLibelle(livrable.getPhase() != null ? livrable.getPhase().getLibelle() : null)
                .build();
    }
}