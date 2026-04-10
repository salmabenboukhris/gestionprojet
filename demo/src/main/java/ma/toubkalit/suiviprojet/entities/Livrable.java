package ma.toubkalit.suiviprojet.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "livrables")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Livrable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 150)
    private String libelle;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 255)
    private String chemin;

    @Column(nullable = false, length = 20, columnDefinition = "VARCHAR(20) DEFAULT 'EN_ATTENTE'")
    @Builder.Default
    private String statut = "EN_ATTENTE"; // EN_ATTENTE, EN_COURS, TERMINE, VALIDE

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phase_id", nullable = false)
    private Phase phase;
}