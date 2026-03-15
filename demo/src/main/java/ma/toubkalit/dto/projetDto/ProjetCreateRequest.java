package ma.toubkalit.dto.projetDto;


import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ProjetCreateRequest {

    @NotBlank(message = "Le code est obligatoire")
    @Size(min = 3, max = 50, message = "Le code doit contenir entre 3 et 50 caractères")
    private String code;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 3, max = 150, message = "Le nom doit contenir entre 3 et 150 caractères")
    private String nom;

    private String description;

    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate dateFin;

    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le montant doit être positif")
    private Double montant;

    @NotNull(message = "L'organisme est obligatoire")
    private Integer organismeId;

    @NotNull(message = "Le chef de projet est obligatoire")
    private Integer chefProjetId;
}