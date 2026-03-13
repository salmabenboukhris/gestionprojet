package ma.toubkalit.suiviprojet.organisation;

import jakarta.persistence.*;

@Entity
public class Employe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String matricule;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String login;
    private String password;

    @ManyToOne
    private Profil profil;
}