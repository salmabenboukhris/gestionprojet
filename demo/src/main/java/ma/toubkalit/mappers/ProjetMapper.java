package ma.toubkalit.mappers;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.dto.ProjetDTO;
import ma.toubkalit.entity.projet.Projet;
import ma.toubkalit.repositories.organisationRepo.EmployeRepo;
import ma.toubkalit.repositories.projetRepo.OrganismeRepo;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjetMapper {

    private final OrganismeRepo organismeRepo;
    private final EmployeRepo employeRepo;

    public ProjetDTO toDTO(Projet entity) {
        if (entity == null) return null;
        return ProjetDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .nom(entity.getNom())
                .description(entity.getDescription())
                .dateDebut(entity.getDateDebut())
                .dateFin(entity.getDateFin())
                .montant(entity.getMontant())
                .organismeId(entity.getOrganisme() != null ? entity.getOrganisme().getId() : null)
                .organismeNom(entity.getOrganisme() != null ? entity.getOrganisme().getNom() : null)
                .chefProjetId(entity.getChefProjet() != null ? entity.getChefProjet().getId() : null)
                .chefProjetNom(entity.getChefProjet() != null ? entity.getChefProjet().getNom() + " " + entity.getChefProjet().getPrenom() : null)
                .build();
    }

    public Projet toEntity(ProjetDTO dto) {
        if (dto == null) return null;
        Projet projet = Projet.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .nom(dto.getNom())
                .description(dto.getDescription())
                .dateDebut(dto.getDateDebut())
                .dateFin(dto.getDateFin())
                .montant(dto.getMontant())
                .build();

        if (dto.getOrganismeId() != null) {
            organismeRepo.findById(dto.getOrganismeId()).ifPresent(projet::setOrganisme);
        }

        if (dto.getChefProjetId() != null) {
            employeRepo.findById(dto.getChefProjetId()).ifPresent(projet::setChefProjet);
        }

        return projet;
    }
}
