package ma.toubkalit.suiviprojet.mappers;

import ma.toubkalit.suiviprojet.dto.projet.ProjetRequestDto;
import ma.toubkalit.suiviprojet.dto.projet.ProjetResponseDto;
import ma.toubkalit.suiviprojet.dto.projet.ProjetResumeDto;
import ma.toubkalit.suiviprojet.entities.Employe;
import ma.toubkalit.suiviprojet.entities.Organisme;
import ma.toubkalit.suiviprojet.entities.Projet;
import org.springframework.stereotype.Component;

@Component
public class ProjetMapper {

    public Projet toEntity(ProjetRequestDto dto, Organisme organisme, Employe chefProjet) {
        return Projet.builder()
                .code(dto.getCode())
                .nom(dto.getNom())
                .description(dto.getDescription())
                .dateDebut(dto.getDateDebut())
                .dateFin(dto.getDateFin())
                .montant(dto.getMontant())
                .organisme(organisme)
                .chefProjet(chefProjet)
                .build();
    }

    public void updateEntity(Projet projet, ProjetRequestDto dto, Organisme organisme, Employe chefProjet) {
        projet.setCode(dto.getCode());
        projet.setNom(dto.getNom());
        projet.setDescription(dto.getDescription());
        projet.setDateDebut(dto.getDateDebut());
        projet.setDateFin(dto.getDateFin());
        projet.setMontant(dto.getMontant());
        projet.setOrganisme(organisme);
        projet.setChefProjet(chefProjet);
    }

    public ProjetResponseDto toResponseDto(Projet projet) {
        return ProjetResponseDto.builder()
                .id(projet.getId())
                .code(projet.getCode())
                .nom(projet.getNom())
                .description(projet.getDescription())
                .dateDebut(projet.getDateDebut())
                .dateFin(projet.getDateFin())
                .montant(projet.getMontant())
                .organismeId(projet.getOrganisme() != null ? projet.getOrganisme().getId() : null)
                .organismeCode(projet.getOrganisme() != null ? projet.getOrganisme().getCode() : null)
                .organismeNom(projet.getOrganisme() != null ? projet.getOrganisme().getNom() : null)
                .chefProjetId(projet.getChefProjet() != null ? projet.getChefProjet().getId() : null)
                .chefProjetNomComplet(
                        projet.getChefProjet() != null
                                ? projet.getChefProjet().getNom() + " " + projet.getChefProjet().getPrenom()
                                : null
                )
                .build();
    }

    public ProjetResumeDto toResumeDto(Projet projet) {
        return ProjetResumeDto.builder()
                .id(projet.getId())
                .code(projet.getCode())
                .nom(projet.getNom())
                .description(projet.getDescription())
                .dateDebut(projet.getDateDebut())
                .dateFin(projet.getDateFin())
                .montant(projet.getMontant())
                .organismeNom(projet.getOrganisme() != null ? projet.getOrganisme().getNom() : null)
                .chefProjetNomComplet(
                        projet.getChefProjet() != null
                                ? projet.getChefProjet().getNom() + " " + projet.getChefProjet().getPrenom()
                                : null
                )
                .nombrePhases(projet.getPhases() != null ? projet.getPhases().size() : 0)
                .nombreDocuments(projet.getDocuments() != null ? projet.getDocuments().size() : 0)
                .build();
    }
}