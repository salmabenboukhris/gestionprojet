package ma.toubkalit.entity.projet;

import jakarta.persistence.*;
import java.util.Date;
import ma.toubkalit.entity.organisation.Employe;

@Entity
public class LigneEmployePhase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date dateDebut;
    private Date dateFin;

    @ManyToOne
    private Employe employe;

    @ManyToOne
    private Phase phase;
}