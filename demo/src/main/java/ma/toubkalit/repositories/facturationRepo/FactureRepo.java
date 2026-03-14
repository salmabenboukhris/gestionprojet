package ma.toubkalit.repositories.facturationRepo;

import ma.toubkalit.entity.facturation.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FactureRepo extends JpaRepository<Facture, Integer> {

    Optional<Facture> findByCode(String code);

}