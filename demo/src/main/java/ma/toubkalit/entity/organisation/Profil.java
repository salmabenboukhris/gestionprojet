package ma.toubkalit.entity.organisation;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "profil")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String code;

    private String libelle;
}