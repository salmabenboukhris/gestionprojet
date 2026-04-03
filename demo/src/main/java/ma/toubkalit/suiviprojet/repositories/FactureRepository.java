package ma.toubkalit.suiviprojet.repositories;

import ma.toubkalit.suiviprojet.entities.Facture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FactureRepository extends JpaRepository<Facture, Long> {

    Optional<Facture> findByCode(String code);

    Optional<Facture> findByPhaseId(Long phaseId);

    boolean existsByCode(String code);

    boolean existsByPhaseId(Long phaseId);
}