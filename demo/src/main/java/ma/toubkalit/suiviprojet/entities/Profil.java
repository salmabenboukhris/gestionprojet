package ma.toubkalit.suiviprojet.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profils")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String libelle;

    @OneToMany(mappedBy = "profil", fetch = FetchType.LAZY)
    private List<Employe> employes = new ArrayList<>();
}