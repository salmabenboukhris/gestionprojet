package ma.toubkalit.entity.projet;

import jakarta.persistence.*;

@Entity
public class Livrable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String libelle;
    private String description;
    private String chemin;

    @ManyToOne
    private Phase phase;
}