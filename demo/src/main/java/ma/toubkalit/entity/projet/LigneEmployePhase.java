package ma.toubkalit.entity.projet;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.toubkalit.entity.organisation.Employe;

import java.time.LocalDate;

@Entity
@Table(name = "ligne_employe_phase")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneEmployePhase {

    @EmbeddedId
    private LigneEmployePhaseId id;

    @Column(nullable = false)
    private LocalDate dateDebut;

    @Column(nullable = false)
    private LocalDate dateFin;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("employeId")
    @JoinColumn(name = "employe_id", nullable = false)
    private Employe employe;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("phaseId")
    @JoinColumn(name = "phase_id", nullable = false)
    private Phase phase;
}