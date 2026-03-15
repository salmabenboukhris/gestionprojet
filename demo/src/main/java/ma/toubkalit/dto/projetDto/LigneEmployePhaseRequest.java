package ma.toubkalit.dto.projetDto;


import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class LigneEmployePhaseRequest {

    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate dateFin;

    private Double chargeHoraire;
}