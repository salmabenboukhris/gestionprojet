package ma.toubkalit.dto.organisationDto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EmployeUpdateRequest {

    @NotBlank(message = "Le matricule est obligatoire")
    @Size(min = 3, max = 50, message = "Le matricule doit contenir entre 3 et 50 caractères")
    private String matricule;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 100, message = "Le prénom doit contenir entre 2 et 100 caractères")
    private String prenom;

    private String telephone;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    @NotBlank(message = "Le login est obligatoire")
    @Size(min = 3, max = 100, message = "Le login doit contenir entre 3 et 100 caractères")
    private String login;

    private String password;

    @NotNull(message = "L'ID du profil est obligatoire")
    private Integer profilId;
}
