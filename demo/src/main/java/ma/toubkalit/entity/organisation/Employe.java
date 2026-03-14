package ma.toubkalit.entity.organisation;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employe")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String matricule;

    private String nom;

    private String prenom;

    private String telephone;

    private String email;

    private String login;

    private String password;

    @ManyToOne
    @JoinColumn(name = "profil_id")
    private Profil profil;
}