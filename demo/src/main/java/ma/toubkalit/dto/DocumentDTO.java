package ma.toubkalit.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO {
    private Integer id;
    private String code;
    private String libelle;
    private String description;
    private String chemin;
    private Integer projetId;
}
