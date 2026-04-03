package ma.toubkalit.suiviprojet.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "affectations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Affectation {

    @EmbeddedId
    private AffectationId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("employeId")
    @JoinColumn(name = "employe_id", nullable = false)
    private Employe employe;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("phaseId")
    @JoinColumn(name = "phase_id", nullable = false)
    private Phase phase;

    @Column(nullable = false)
    private LocalDate dateDebut;

    @Column(nullable = false)
    private LocalDate dateFin;
}