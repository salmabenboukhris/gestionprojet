package ma.toubkalit.suiviprojet.dto.document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentRequestDto {

    @NotBlank(message = "Le code est obligatoire")
    @Size(max = 50)
    private String code;

    @NotBlank(message = "Le libellé est obligatoire")
    @Size(max = 150)
    private String libelle;

    private String description;

    @Size(max = 255)
    private String chemin;
}