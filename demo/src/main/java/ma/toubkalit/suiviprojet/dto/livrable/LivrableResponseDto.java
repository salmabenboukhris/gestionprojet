package ma.toubkalit.suiviprojet.dto.livrable;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LivrableResponseDto {

    private Long id;
    private String code;
    private String libelle;
    private String description;
    private String chemin;

    private Long phaseId;
    private String phaseCode;
    private String phaseLibelle;
}