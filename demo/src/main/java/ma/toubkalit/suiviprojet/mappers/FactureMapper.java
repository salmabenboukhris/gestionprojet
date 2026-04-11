package ma.toubkalit.suiviprojet.mappers;

import ma.toubkalit.suiviprojet.dto.facture.FactureRequestDto;
import ma.toubkalit.suiviprojet.dto.facture.FactureResponseDto;
import ma.toubkalit.suiviprojet.entities.Facture;
import ma.toubkalit.suiviprojet.entities.Phase;
import org.springframework.stereotype.Component;

@Component
public class FactureMapper {

    public Facture toEntity(FactureRequestDto dto, Phase phase) {
        return Facture.builder()
                .code(dto.getCode())
                .dateFacture(dto.getDateFacture())
                .montant(dto.getMontant())
                .statut(dto.getStatut() != null ? dto.getStatut() : "EN_ATTENTE")
                .phase(phase)
                .build();
    }

    public void updateEntity(Facture facture, FactureRequestDto dto) {
        facture.setCode(dto.getCode());
        facture.setDateFacture(dto.getDateFacture());
        if (dto.getMontant() != null) facture.setMontant(dto.getMontant());
        if (dto.getStatut() != null)  facture.setStatut(dto.getStatut());
    }

    public FactureResponseDto toResponseDto(Facture facture) {
        return FactureResponseDto.builder()
                .id(facture.getId())
                .code(facture.getCode())
                .dateFacture(facture.getDateFacture())
                .montant(facture.getMontant())
                .statut(facture.getStatut())
                .phaseId(facture.getPhase() != null ? facture.getPhase().getId() : null)
                .phaseCode(facture.getPhase() != null ? facture.getPhase().getCode() : null)
                .phaseLibelle(facture.getPhase() != null ? facture.getPhase().getLibelle() : null)
                .etatRealisation(facture.getPhase() != null ? facture.getPhase().getEtatRealisation() : null)
                .etatFacturation(facture.getPhase() != null ? facture.getPhase().getEtatFacturation() : null)
                .etatPaiement(facture.getPhase() != null ? facture.getPhase().getEtatPaiement() : null)
                .build();
    }
}