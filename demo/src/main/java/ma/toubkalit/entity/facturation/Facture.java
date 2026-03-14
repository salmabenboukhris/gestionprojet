package ma.toubkalit.entity.facturation;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import ma.toubkalit.entity.projet.Phase;

@Entity
@Table(name = "factures")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String code;

    private LocalDate dateFacture;

    @OneToOne
    @JoinColumn(name = "phase_id")
    private Phase phase;
}