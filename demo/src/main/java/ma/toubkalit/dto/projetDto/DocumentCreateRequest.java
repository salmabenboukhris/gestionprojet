package ma.toubkalit.dto.projetDto;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DocumentCreateRequest {

    @NotBlank(message = "Le code est obligatoire")
    @Size(min = 3, max = 50, message = "Le code doit contenir entre 3 et 50 caractères")
    private String code;

    @NotBlank(message = "Le libellé est obligatoire")
    @Size(min = 3, max = 150, message = "Le libellé doit contenir entre 3 et 150 caractères")
    private String libelle;

    private String description;
    private String chemin;
}