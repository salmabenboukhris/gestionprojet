package ma.toubkalit.repositories.organisationRepo;

import ma.toubkalit.entity.organisation.Profil;
import ma.toubkalit.enums.RoleCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfilRepo extends JpaRepository<Profil, Integer> {
    Optional<Profil> findByRoleCode(RoleCode roleCode);
}