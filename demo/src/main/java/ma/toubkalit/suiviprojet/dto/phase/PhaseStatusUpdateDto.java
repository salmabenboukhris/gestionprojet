package ma.toubkalit.suiviprojet.dto.phase;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhaseStatusUpdateDto {

    @NotNull(message = "Le statut est obligatoire")
    private Boolean value;
}