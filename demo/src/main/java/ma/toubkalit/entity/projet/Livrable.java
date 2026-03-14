package ma.toubkalit.entity.projet;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "livrable")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Livrable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String code;

    private String libelle;

    private String description;

    private String chemin;

    @ManyToOne
    @JoinColumn(name = "phase_id")
    private Phase phase;
}