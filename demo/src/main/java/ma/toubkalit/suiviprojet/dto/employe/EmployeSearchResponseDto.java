package ma.toubkalit.suiviprojet.dto.employe;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeSearchResponseDto {

    private Long id;
    private String matricule;
    private String nom;
    private String prenom;
    private String email;
    private String login;
}