package ma.toubkalit.mappers;

import ma.toubkalit.dto.OrganismeDTO;
import ma.toubkalit.entity.projet.Organisme;
import org.springframework.stereotype.Component;

@Component
public class OrganismeMapper {

    public OrganismeDTO toDTO(Organisme entity) {
        if (entity == null) return null;
        return OrganismeDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .nom(entity.getNom())
                .adresse(entity.getAdresse())
                .telephone(entity.getTelephone())
                .nomContact(entity.getNomContact())
                .emailContact(entity.getEmailContact())
                .siteWeb(entity.getSiteWeb())
                .build();
    }

    public Organisme toEntity(OrganismeDTO dto) {
        if (dto == null) return null;
        return Organisme.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .nom(dto.getNom())
                .adresse(dto.getAdresse())
                .telephone(dto.getTelephone())
                .nomContact(dto.getNomContact())
                .emailContact(dto.getEmailContact())
                .siteWeb(dto.getSiteWeb())
                .build();
    }
}
