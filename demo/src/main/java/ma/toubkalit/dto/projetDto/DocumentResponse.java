package ma.toubkalit.dto.projetDto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentResponse {
    private Integer id;
    private String code;
    private String libelle;
    private String description;
    private String chemin;

    // Informations du projet
    private Integer projetId;
    private String projetCode;
    private String projetNom;
}
