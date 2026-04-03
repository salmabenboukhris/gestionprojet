package ma.toubkalit.suiviprojet.dto.employe;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeRequestDto {

    @NotBlank(message = "Le matricule est obligatoire")
    @Size(max = 50)
    private String matricule;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 100)
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(max = 100)
    private String prenom;

    @Size(max = 30)
    private String telephone;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Email invalide")
    @Size(max = 150)
    private String email;

    @NotBlank(message = "Le login est obligatoire")
    @Size(max = 100)
    private String login;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(max = 255)
    private String password;

    @NotNull(message = "Le profil est obligatoire")
    private Long profilId;
}