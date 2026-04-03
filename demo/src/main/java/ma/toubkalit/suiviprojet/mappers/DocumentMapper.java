package ma.toubkalit.suiviprojet.mappers;

import ma.toubkalit.suiviprojet.dto.document.DocumentDownloadDto;
import ma.toubkalit.suiviprojet.dto.document.DocumentRequestDto;
import ma.toubkalit.suiviprojet.dto.document.DocumentResponseDto;
import ma.toubkalit.suiviprojet.entities.Document;
import ma.toubkalit.suiviprojet.entities.Projet;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {

    public Document toEntity(DocumentRequestDto dto, Projet projet) {
        return Document.builder()
                .code(dto.getCode())
                .libelle(dto.getLibelle())
                .description(dto.getDescription())
                .chemin(dto.getChemin())
                .projet(projet)
                .build();
    }

    public void updateEntity(Document document, DocumentRequestDto dto) {
        document.setCode(dto.getCode());
        document.setLibelle(dto.getLibelle());
        document.setDescription(dto.getDescription());
        document.setChemin(dto.getChemin());
    }

    public DocumentResponseDto toResponseDto(Document document) {
        return DocumentResponseDto.builder()
                .id(document.getId())
                .code(document.getCode())
                .libelle(document.getLibelle())
                .description(document.getDescription())
                .chemin(document.getChemin())
                .projetId(document.getProjet() != null ? document.getProjet().getId() : null)
                .projetCode(document.getProjet() != null ? document.getProjet().getCode() : null)
                .projetNom(document.getProjet() != null ? document.getProjet().getNom() : null)
                .build();
    }

    public DocumentDownloadDto toDownloadDto(Document document) {
        return DocumentDownloadDto.builder()
                .id(document.getId())
                .code(document.getCode())
                .libelle(document.getLibelle())
                .chemin(document.getChemin())
                .message("Téléchargement simulé : utiliser le chemin fourni")
                .build();
    }
}