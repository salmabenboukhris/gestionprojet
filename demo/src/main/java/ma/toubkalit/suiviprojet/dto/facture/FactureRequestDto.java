package ma.toubkalit.suiviprojet.dto.facture;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FactureRequestDto {

    @NotBlank(message = "Le code est obligatoire")
    @Size(max = 50)
    private String code;

    @NotNull(message = "La date de facture est obligatoire")
    private LocalDate dateFacture;
}