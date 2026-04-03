package ma.toubkalit.suiviprojet.dto.organisme;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganismeRequestDto {

    @NotBlank(message = "Le code est obligatoire")
    @Size(max = 50, message = "Le code ne doit pas dépasser 50 caractères")
    private String code;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 150, message = "Le nom ne doit pas dépasser 150 caractères")
    private String nom;

    @Size(max = 255, message = "L'adresse ne doit pas dépasser 255 caractères")
    private String adresse;

    @Size(max = 30, message = "Le téléphone ne doit pas dépasser 30 caractères")
    private String telephone;

    @Size(max = 150, message = "Le nom du contact ne doit pas dépasser 150 caractères")
    private String nomContact;

    @Email(message = "L'email du contact est invalide")
    @Size(max = 150, message = "L'email du contact ne doit pas dépasser 150 caractères")
    private String emailContact;

    @Size(max = 150, message = "Le site web ne doit pas dépasser 150 caractères")
    private String siteWeb;
}