package ma.toubkalit.mappers;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.dto.PhaseDTO;
import ma.toubkalit.entity.projet.Phase;
import ma.toubkalit.repositories.projetRepo.ProjetRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PhaseMapper {

    private final ProjetRepository projetRepo;

    public PhaseDTO toDTO(Phase entity) {
        if (entity == null) return null;
        return PhaseDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .libelle(entity.getLibelle())
                .description(entity.getDescription())
                .dateDebut(entity.getDateDebut())
                .dateFin(entity.getDateFin())
                .montant(entity.getMontant())
                .etatRealisation(entity.getEtatRealisation())
                .etatFacturation(entity.getEtatFacturation())
                .etatPaiement(entity.getEtatPaiement())
                .projetId(entity.getProjet() != null ? entity.getProjet().getId() : null)
                .build();
    }

    public Phase toEntity(PhaseDTO dto) {
        if (dto == null) return null;
        Phase phase = Phase.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .libelle(dto.getLibelle())
                .description(dto.getDescription())
                .dateDebut(dto.getDateDebut())
                .dateFin(dto.getDateFin())
                .montant(dto.getMontant())
                .etatRealisation(dto.getEtatRealisation())
                .etatFacturation(dto.getEtatFacturation())
                .etatPaiement(dto.getEtatPaiement())
                .build();

        if (dto.getProjetId() != null) {
            projetRepo.findById(dto.getProjetId()).ifPresent(phase::setProjet);
        }

        return phase;
    }
}
