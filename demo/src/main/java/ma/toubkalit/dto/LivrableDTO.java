package ma.toubkalit.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LivrableDTO {
    private Integer id;
    private String code;
    private String libelle;
    private String description;
    private String chemin;
    private Integer phaseId;
}
