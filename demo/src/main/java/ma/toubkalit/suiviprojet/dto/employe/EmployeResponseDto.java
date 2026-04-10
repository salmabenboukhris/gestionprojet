package ma.toubkalit.suiviprojet.dto.employe;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeResponseDto {

    private Long id;
    private String matricule;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String login;
    private Long profilId;
    private String profilCode;
    private String profilLibelle;
}