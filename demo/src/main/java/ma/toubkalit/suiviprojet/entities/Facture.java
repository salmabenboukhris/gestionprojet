package ma.toubkalit.suiviprojet.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "factures",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_facture_code", columnNames = "code"),
                @UniqueConstraint(name = "uk_facture_phase", columnNames = "phase_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false)
    private LocalDate dateFacture;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phase_id", nullable = false, unique = true)
    private Phase phase;
}