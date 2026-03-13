package ma.toubkalit.suiviprojet.facturation;

import jakarta.persistence.*;
import java.util.Date;
import ma.toubkalit.suiviprojet.projet.Phase;

@Entity
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private Date dateFacture;

    @OneToOne
    private Phase phase;
}