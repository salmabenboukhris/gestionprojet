package ma.toubkalit.suiviprojet.dto.document;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentResponseDto {

    private Long id;
    private String code;
    private String libelle;
    private String description;
    private String chemin;

    // Métadonnées fichier
    private String typeFichier;
    private Long taille;
    private LocalDateTime dateUpload;

    private Long projetId;
    private String projetCode;
    private String projetNom;
}