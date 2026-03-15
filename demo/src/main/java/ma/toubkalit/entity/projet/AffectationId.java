package ma.toubkalit.entity.projet;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class AffectationId implements Serializable {

    @Column(name = "employe_id")
    private Integer employeId;

    @Column(name = "phase_id")
    private Integer phaseId;
}
