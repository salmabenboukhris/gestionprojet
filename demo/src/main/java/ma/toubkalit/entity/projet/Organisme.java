package ma.toubkalit.entity.projet;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "organisme")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organisme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;

    private String nom;

    private String adresse;

    private String telephone;

    private String nomContact;

    private String emailContact;

    private String siteWeb;
}