package ma.toubkalit.suiviprojet.repositories;

import ma.toubkalit.suiviprojet.entities.Affectation;
import ma.toubkalit.suiviprojet.entities.AffectationId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AffectationRepository extends JpaRepository<Affectation, AffectationId> {

    List<Affectation> findByEmployeId(Long employeId);

    List<Affectation> findByPhaseId(Long phaseId);

    Optional<Affectation> findByEmployeIdAndPhaseId(Long employeId, Long phaseId);

    boolean existsByEmployeIdAndPhaseId(Long employeId, Long phaseId);
}