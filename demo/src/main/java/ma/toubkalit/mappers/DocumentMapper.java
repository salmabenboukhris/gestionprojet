package ma.toubkalit.mappers;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.dto.DocumentDTO;
import ma.toubkalit.entity.projet.Document;
import ma.toubkalit.repositories.projetRepo.ProjetRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DocumentMapper {

    private final ProjetRepository projetRepo;

    public DocumentDTO toDTO(Document entity) {
        if (entity == null) return null;
        return DocumentDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .libelle(entity.getLibelle())
                .description(entity.getDescription())
                .chemin(entity.getChemin())
                .projetId(entity.getProjet() != null ? entity.getProjet().getId() : null)
                .build();
    }

    public Document toEntity(DocumentDTO dto) {
        if (dto == null) return null;
        Document document = Document.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .libelle(dto.getLibelle())
                .description(dto.getDescription())
                .chemin(dto.getChemin())
                .build();

        if (dto.getProjetId() != null) {
            projetRepo.findById(dto.getProjetId()).ifPresent(document::setProjet);
        }

        return document;
    }
}
