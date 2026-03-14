package ma.toubkalit.repositories.projetRepo;

import ma.toubkalit.entity.projet.Livrable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivrableRepo extends JpaRepository<Livrable, Integer> {

}