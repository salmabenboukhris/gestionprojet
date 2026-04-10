package ma.toubkalit.suiviprojet.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "employes",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_employe_matricule", columnNames = "matricule"),
                @UniqueConstraint(name = "uk_employe_login", columnNames = "login"),
                @UniqueConstraint(name = "uk_employe_email", columnNames = "email")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String matricule;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(length = 30)
    private String telephone;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, unique = true, length = 100)
    private String login;

    @Column(nullable = false, length = 255)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profil_id", nullable = false)
    private Profil profil;


    @OneToMany(mappedBy = "chefProjet", fetch = FetchType.LAZY)
    private List<Projet> projetsDiriges = new ArrayList<>();

    @OneToMany(mappedBy = "employe", fetch = FetchType.LAZY)
    private List<Affectation> affectations = new ArrayList<>();
}