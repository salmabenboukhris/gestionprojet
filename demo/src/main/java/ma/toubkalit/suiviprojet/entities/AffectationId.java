package ma.toubkalit.suiviprojet.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AffectationId implements Serializable {

    @Column(name = "employe_id")
    private Long employeId;

    @Column(name = "phase_id")
    private Long phaseId;
}