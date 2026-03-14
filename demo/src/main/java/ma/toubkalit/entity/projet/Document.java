package ma.toubkalit.entity.projet;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "document")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String code;

    private String libelle;

    private String description;

    private String chemin;

    @ManyToOne
    @JoinColumn(name = "projet_id")
    private Projet projet;
}