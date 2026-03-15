package ma.toubkalit.dto.organisationDto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfilResponse {
    private Integer id;
    private String code;
    private String libelle;
    private Integer nombreEmployes;
}