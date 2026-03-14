package ma.toubkalit.repositories.projetRepo;

import ma.toubkalit.entity.projet.LigneEmployePhase;
import ma.toubkalit.entity.projet.LigneEmployePhaseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LigneEmployePhaseRepo extends JpaRepository<LigneEmployePhase, LigneEmployePhaseId> {

}