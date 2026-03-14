package ma.toubkalit.entity.projet;

import jakarta.persistence.*;

@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String libelle;
    private String description;
    private String chemin;

    @ManyToOne
    private Projet projet;
}