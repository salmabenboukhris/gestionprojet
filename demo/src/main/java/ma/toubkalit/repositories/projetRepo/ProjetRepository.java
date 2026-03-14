package ma.toubkalit.repositories.projetRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import ma.toubkalit.entity.projet.Projet;

public interface ProjetRepository extends JpaRepository<Projet, Long> {
}