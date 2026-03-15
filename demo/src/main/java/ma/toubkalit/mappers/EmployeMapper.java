package ma.toubkalit.mappers;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.dto.EmployeDTO;
import ma.toubkalit.entity.organisation.Employe;
import ma.toubkalit.repositories.organisationRepo.ProfilRepo;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeMapper {

    private final ProfilRepo profilRepo;

    public EmployeDTO toDTO(Employe entity) {
        if (entity == null) return null;
        return EmployeDTO.builder()
                .id(entity.getId())
                .matricule(entity.getMatricule())
                .nom(entity.getNom())
                .prenom(entity.getPrenom())
                .telephone(entity.getTelephone())
                .email(entity.getEmail())
                .login(entity.getLogin())
                .profilId(entity.getProfil() != null ? entity.getProfil().getId() : null)
                .profilLibelle(entity.getProfil() != null ? entity.getProfil().getLibelle() : null)
                .build();
    }

    public Employe toEntity(EmployeDTO dto) {
        if (dto == null) return null;
        Employe employe = Employe.builder()
                .id(dto.getId())
                .matricule(dto.getMatricule())
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .telephone(dto.getTelephone())
                .email(dto.getEmail())
                .login(dto.getLogin())
                .password(dto.getPassword())
                .build();
        
        if (dto.getProfilId() != null) {
            profilRepo.findById(dto.getProfilId()).ifPresent(employe::setProfil);
        }
        
        return employe;
    }
}
