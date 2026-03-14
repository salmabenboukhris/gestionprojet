package ma.toubkalit.entity.projet;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Phase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String libelle;
    private String description;

    private Date dateDebut;
    private Date dateFin;

    private double montant;

    private boolean etatRealisation;
    private boolean etatFacturation;
    private boolean etatPaiement;

    @ManyToOne
    private Projet projet;
}