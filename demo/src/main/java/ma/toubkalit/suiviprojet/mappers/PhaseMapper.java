package ma.toubkalit.suiviprojet.mappers;

import ma.toubkalit.suiviprojet.dto.phase.PhaseRequestDto;
import ma.toubkalit.suiviprojet.dto.phase.PhaseResponseDto;
import ma.toubkalit.suiviprojet.entities.Phase;
import ma.toubkalit.suiviprojet.entities.Projet;
import org.springframework.stereotype.Component;

@Component
public class PhaseMapper {

    public Phase toEntity(PhaseRequestDto dto, Projet projet) {
        return Phase.builder()
                .code(dto.getCode())
                .libelle(dto.getLibelle())
                .description(dto.getDescription())
                .dateDebut(dto.getDateDebut())
                .dateFin(dto.getDateFin())
                .montant(dto.getMontant())
                .tauxRealisation(dto.getTauxRealisation() != null ? dto.getTauxRealisation() : 0)
                .etatRealisation(false)
                .etatFacturation(false)
                .etatPaiement(false)
                .projet(projet)
                .build();
    }

    public void updateEntity(Phase phase, PhaseRequestDto dto, Projet projet) {
        phase.setCode(dto.getCode());
        phase.setLibelle(dto.getLibelle());
        phase.setDescription(dto.getDescription());
        phase.setDateDebut(dto.getDateDebut());
        phase.setDateFin(dto.getDateFin());
        phase.setMontant(dto.getMontant());
        if (dto.getTauxRealisation() != null) {
            phase.setTauxRealisation(dto.getTauxRealisation());
        }
        phase.setProjet(projet);
    }

    public PhaseResponseDto toResponseDto(Phase phase) {
        return PhaseResponseDto.builder()
                .id(phase.getId())
                .code(phase.getCode())
                .libelle(phase.getLibelle())
                .description(phase.getDescription())
                .dateDebut(phase.getDateDebut())
                .dateFin(phase.getDateFin())
                .montant(phase.getMontant())
                .etatRealisation(phase.getEtatRealisation())
                .etatFacturation(phase.getEtatFacturation())
                .etatPaiement(phase.getEtatPaiement())
                .tauxRealisation(phase.getTauxRealisation())
                .projetId(phase.getProjet() != null ? phase.getProjet().getId() : null)
                .projetCode(phase.getProjet() != null ? phase.getProjet().getCode() : null)
                .projetNom(phase.getProjet() != null ? phase.getProjet().getNom() : null)
                .build();
    }
}