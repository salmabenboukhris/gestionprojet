package ma.toubkalit.repositories.projetRepo;

import ma.toubkalit.entity.projet.Affectation;
import ma.toubkalit.entity.projet.AffectationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AffectationRepo extends JpaRepository<Affectation, AffectationId> {
    List<Affectation> findByPhaseId(Integer phaseId);
    List<Affectation> findByEmployeId(Integer employeId);
}
