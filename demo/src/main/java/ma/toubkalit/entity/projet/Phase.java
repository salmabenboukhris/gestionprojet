package ma.toubkalit.entity.projet;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;
import ma.toubkalit.enums.*;

@Entity
@Table(name = "phase")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Phase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String code;

    private String libelle;

    private String description;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    private double montant;

    @Enumerated(EnumType.STRING)
    private EtatRealisation etatRealisation;

    @Enumerated(EnumType.STRING)
    private EtatFacturation etatFacturation;

    @Enumerated(EnumType.STRING)
    private EtatPaiement etatPaiement;

    @ManyToOne
    @JoinColumn(name = "projet_id")
    private Projet projet;

    @OneToMany(mappedBy = "phase")
    private List<Livrable> livrables;
}