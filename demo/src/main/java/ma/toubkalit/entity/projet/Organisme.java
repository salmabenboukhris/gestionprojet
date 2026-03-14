package ma.toubkalit.entity.projet;

import jakarta.persistence.*;

@Entity
public class Organisme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String nom;
    private String adresse;
    private String telephone;
    private String nomContact;
    private String emailContact;
    private String siteWeb;
}