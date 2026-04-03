package ma.toubkalit.suiviprojet.dto.document;

import lombok.*;

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

    private Long projetId;
    private String projetCode;
    private String projetNom;
}