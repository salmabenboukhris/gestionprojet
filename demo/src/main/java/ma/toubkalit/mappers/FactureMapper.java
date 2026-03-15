package ma.toubkalit.mappers;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.dto.FactureDTO;
import ma.toubkalit.entity.facturation.Facture;
import ma.toubkalit.repositories.projetRepo.PhaseRepo;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FactureMapper {

    private final PhaseRepo phaseRepo;

    public FactureDTO toDTO(Facture entity) {
        if (entity == null) return null;
        return FactureDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .dateFacture(entity.getDateFacture())
                .montant(entity.getMontant())
                .description(entity.getDescription())
                .phaseId(entity.getPhase() != null ? entity.getPhase().getId() : null)
                .build();
    }

    public Facture toEntity(FactureDTO dto) {
        if (dto == null) return null;
        Facture facture = Facture.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .dateFacture(dto.getDateFacture())
                .montant(dto.getMontant())
                .description(dto.getDescription())
                .build();

        if (dto.getPhaseId() != null) {
            phaseRepo.findById(dto.getPhaseId()).ifPresent(facture::setPhase);
        }

        return facture;
    }
}
