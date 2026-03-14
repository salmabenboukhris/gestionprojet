package ma.toubkalit.entity.projet;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;
import ma.toubkalit.entity.organisation.Employe;

@Entity
@Table(name = "projet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String code;

    private String nom;

    private String description;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    private double montant;

    @ManyToOne
    @JoinColumn(name = "organisme_id")
    private Organisme organisme;

    @ManyToOne
    @JoinColumn(name = "chef_projet_id")
    private Employe chefProjet;

    @OneToMany(mappedBy = "projet")
    private List<Phase> phases;

    @OneToMany(mappedBy = "projet")
    private List<Document> documents;
}