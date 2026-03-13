package ma.toubkalit.suiviprojet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ma.toubkalit.suiviprojet.projet.Projet;

public interface ProjetRepository extends JpaRepository<Projet, Long> {
}