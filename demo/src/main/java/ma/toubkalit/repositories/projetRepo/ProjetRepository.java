package ma.toubkalit.repositories.projetRepo;

import ma.toubkalit.entity.projet.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjetRepository extends JpaRepository<Projet, Integer> {

    Optional<Projet> findByCode(String code);

}