package ma.toubkalit.suiviprojet.mappers;

import ma.toubkalit.suiviprojet.dto.employe.EmployeRequestDto;
import ma.toubkalit.suiviprojet.dto.employe.EmployeResponseDto;
import ma.toubkalit.suiviprojet.dto.employe.EmployeSearchResponseDto;
import ma.toubkalit.suiviprojet.entities.Employe;
import ma.toubkalit.suiviprojet.entities.Profil;
import org.springframework.stereotype.Component;

@Component
public class EmployeMapper {

    public Employe toEntity(EmployeRequestDto dto, Profil profil) {
        return Employe.builder()
                .matricule(dto.getMatricule())
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .telephone(dto.getTelephone())
                .email(dto.getEmail())
                .login(dto.getLogin())
                .password(dto.getPassword())
                .profil(profil)
                .build();
    }

    public void updateEntity(Employe employe, EmployeRequestDto dto, Profil profil) {
        employe.setMatricule(dto.getMatricule());
        employe.setNom(dto.getNom());
        employe.setPrenom(dto.getPrenom());
        employe.setTelephone(dto.getTelephone());
        employe.setEmail(dto.getEmail());
        employe.setLogin(dto.getLogin());
        employe.setPassword(dto.getPassword());
        employe.setProfil(profil);
    }

    public EmployeResponseDto toResponseDto(Employe employe) {
        return EmployeResponseDto.builder()
                .id(employe.getId())
                .matricule(employe.getMatricule())
                .nom(employe.getNom())
                .prenom(employe.getPrenom())
                .telephone(employe.getTelephone())
                .email(employe.getEmail())
                .login(employe.getLogin())
                .profilId(employe.getProfil() != null ? employe.getProfil().getId() : null)
                .profilCode(employe.getProfil() != null ? employe.getProfil().getCode() : null)
                .profilLibelle(employe.getProfil() != null ? employe.getProfil().getLibelle() : null)
                .build();
    }

    public EmployeSearchResponseDto toSearchDto(Employe employe) {
        return EmployeSearchResponseDto.builder()
                .id(employe.getId())
                .matricule(employe.getMatricule())
                .nom(employe.getNom())
                .prenom(employe.getPrenom())
                .email(employe.getEmail())
                .login(employe.getLogin())
                .build();
    }
}