package ma.toubkalit.entity.projet;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import ma.toubkalit.entity.organisation.Employe;

@Entity
@Table(name = "ligne_employe_phase")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneEmployePhase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    @ManyToOne
    @JoinColumn(name = "employe_id")
    private Employe employe;

    @ManyToOne
    @JoinColumn(name = "phase_id")
    private Phase phase;
}