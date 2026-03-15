package ma.toubkalit.dto.projetDto;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class OrganismeCreateRequest {

    @NotBlank(message = "Le code est obligatoire")
    @Size(min = 2, max = 50, message = "Le code doit contenir entre 2 et 50 caractères")
    private String code;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 150, message = "Le nom doit contenir entre 2 et 150 caractères")
    private String nom;

    private String adresse;
    private String telephone;
    private String nomContact;

    @Email(message = "L'email doit être valide")
    private String emailContact;

    private String siteWeb;
}