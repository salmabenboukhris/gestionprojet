package ma.toubkalit.dto.organisationDto;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProfilCreateRequest {

    @NotBlank(message = "Le code est obligatoire")
    @Size(min = 2, max = 50, message = "Le code doit contenir entre 2 et 50 caractères")
    private String code;

    @NotBlank(message = "Le libellé est obligatoire")
    @Size(min = 2, max = 100, message = "Le libellé doit contenir entre 2 et 100 caractères")
    private String libelle;
}
