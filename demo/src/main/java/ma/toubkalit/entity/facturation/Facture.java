package ma.toubkalit.entity.facturation;

import jakarta.persistence.*;
import java.util.Date;
import ma.toubkalit.entity.projet.Phase;

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