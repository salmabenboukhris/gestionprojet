package ma.toubkalit.dto.facturationDto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class FactureUpdateRequest {

    @NotBlank(message = "Le code est obligatoire")
    @Size(min = 3, max = 50, message = "Le code doit contenir entre 3 et 50 caractères")
    private String code;

    @NotNull(message = "La date de facture est obligatoire")
    private LocalDate dateFacture;
}
