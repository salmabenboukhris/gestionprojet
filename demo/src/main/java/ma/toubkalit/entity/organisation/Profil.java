package ma.toubkalit.entity.organisation;

import ma.toubkalit.enums.RoleCode;

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
    private Integer id;

    @Enumerated(EnumType.STRING)
    private RoleCode roleCode;

    private String code;

    private String libelle;
}