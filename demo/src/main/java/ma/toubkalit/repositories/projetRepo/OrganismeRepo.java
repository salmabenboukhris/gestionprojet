package ma.toubkalit.repositories.projetRepo;

import ma.toubkalit.entity.projet.Organisme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganismeRepo extends JpaRepository<Organisme, Integer> {

    Optional<Organisme> findByCode(String code);

    Optional<Organisme> findByNom(String nom);

    List<Organisme> findByNomContaining(String nom);

    boolean existsByCode(String code);

}