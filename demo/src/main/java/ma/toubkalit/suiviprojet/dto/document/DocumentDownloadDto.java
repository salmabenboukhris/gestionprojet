package ma.toubkalit.suiviprojet.dto.document;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentDownloadDto {

    private Long id;
    private String code;
    private String libelle;
    private String chemin;
    private String message;
}