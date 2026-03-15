package ma.toubkalit.dto.organisationDto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeResponse {
    private Integer id;
    private String matricule;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String login;

    // Informations du profil
    private Integer profilId;
    private String profilCode;
    private String profilLibelle;
}
