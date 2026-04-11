package ma.toubkalit.suiviprojet.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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

    @Column(nullable = false, precision = 15, scale = 2, columnDefinition = "DECIMAL(15,2) DEFAULT 0")
    private BigDecimal montant = java.math.BigDecimal.ZERO;

    @Column(nullable = false, length = 20, columnDefinition = "VARCHAR(20) DEFAULT 'EN_ATTENTE'")
    @Builder.Default
    private String statut = "EN_ATTENTE";

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phase_id", nullable = false, unique = true)
    private Phase phase;
}