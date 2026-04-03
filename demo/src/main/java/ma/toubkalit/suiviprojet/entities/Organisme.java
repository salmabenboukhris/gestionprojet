package ma.toubkalit.suiviprojet.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "organismes",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_organisme_code", columnNames = "code")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organisme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 150)
    private String nom;

    @Column(length = 255)
    private String adresse;

    @Column(length = 30)
    private String telephone;

    @Column(length = 150)
    private String nomContact;

    @Column(length = 150)
    private String emailContact;

    @Column(length = 150)
    private String siteWeb;

    @OneToMany(mappedBy = "organisme", fetch = FetchType.LAZY)
    private List<Projet> projets = new ArrayList<>();
}