package ma.toubkalit.entity.projet;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String nom;
    private String description;

    private Date dateDebut;
    private Date dateFin;

    private double montant;

    @ManyToOne
    private Organisme organisme;
}