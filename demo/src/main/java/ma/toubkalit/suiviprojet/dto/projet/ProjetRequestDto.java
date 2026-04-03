package ma.toubkalit.suiviprojet.dto.projet;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjetRequestDto {

    @NotBlank(message = "Le code est obligatoire")
    @Size(max = 50)
    private String code;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 150)
    private String nom;

    private String description;

    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate dateFin;

    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le montant doit être positif")
    private BigDecimal montant;

    @NotNull(message = "L'organisme est obligatoire")
    private Long organismeId;

    @NotNull(message = "Le chef de projet est obligatoire")
    private Long chefProjetId;
}