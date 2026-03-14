package ma.toubkalit.repositories.organisationRepo;

import ma.toubkalit.entity.organisation.Profil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfilRepo extends JpaRepository<Profil, Integer> {

}