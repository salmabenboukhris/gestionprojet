package ma.toubkalit.entity.organisation;

import jakarta.persistence.*;

@Entity
public class Profil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String libelle;
}