package ma.toubkalit.dto.projetDto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrganismeResponse {
    private Integer id;
    private String code;
    private String nom;
    private String adresse;
    private String telephone;
    private String nomContact;
    private String emailContact;
    private String siteWeb;
    private Integer nombreProjets;
}