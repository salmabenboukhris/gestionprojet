package ma.toubkalit.suiviprojet.services.impl;

import ma.toubkalit.suiviprojet.dto.employe.EmployeRequestDto;
import ma.toubkalit.suiviprojet.dto.employe.EmployeResponseDto;
import ma.toubkalit.suiviprojet.dto.employe.EmployeSearchResponseDto;
import ma.toubkalit.suiviprojet.entities.Employe;
import ma.toubkalit.suiviprojet.entities.Profil;
import ma.toubkalit.suiviprojet.exceptions.DuplicateResourceException;
import ma.toubkalit.suiviprojet.exceptions.OperationNotAllowedException;
import ma.toubkalit.suiviprojet.exceptions.ResourceNotFoundException;
import ma.toubkalit.suiviprojet.mappers.EmployeMapper;
import ma.toubkalit.suiviprojet.repositories.AffectationRepository;
import ma.toubkalit.suiviprojet.repositories.EmployeRepository;
import ma.toubkalit.suiviprojet.repositories.ProfilRepository;
import ma.toubkalit.suiviprojet.services.EmployeService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeServiceImpl implements EmployeService {

    private final EmployeRepository employeRepository;
    private final ProfilRepository profilRepository;
    private final AffectationRepository affectationRepository;
    private final EmployeMapper employeMapper;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public EmployeServiceImpl(EmployeRepository employeRepository,
                              ProfilRepository profilRepository,
                              AffectationRepository affectationRepository,
                              EmployeMapper employeMapper,
                              org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.employeRepository = employeRepository;
        this.profilRepository = profilRepository;
        this.affectationRepository = affectationRepository;
        this.employeMapper = employeMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public EmployeResponseDto create(EmployeRequestDto requestDto) {
        checkUniqueFields(requestDto, null);

        Profil profil = profilRepository.findById(requestDto.getProfilId())
                .orElseThrow(() -> new ResourceNotFoundException("Profil introuvable avec l'id : " + requestDto.getProfilId()));

        Employe employe = employeMapper.toEntity(requestDto, profil);
        employe.setPassword(passwordEncoder.encode(employe.getPassword()));
        Employe saved = employeRepository.save(employe);
        return employeMapper.toResponseDto(saved);
    }

    @Override
    public EmployeResponseDto update(Long id, EmployeRequestDto requestDto) {
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable avec l'id : " + id));

        checkUniqueFields(requestDto, id);

        Profil profil = profilRepository.findById(requestDto.getProfilId())
                .orElseThrow(() -> new ResourceNotFoundException("Profil introuvable avec l'id : " + requestDto.getProfilId()));

        String oldPassword = employe.getPassword();
        employeMapper.updateEntity(employe, requestDto, profil);
        
        if (requestDto.getPassword() != null && !requestDto.getPassword().isBlank()) {
            employe.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        } else {
            employe.setPassword(oldPassword);
        }
        
        Employe updated = employeRepository.save(employe);
        return employeMapper.toResponseDto(updated);
    }

    @Override
    public EmployeResponseDto getById(Long id) {
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable avec l'id : " + id));

        return employeMapper.toResponseDto(employe);
    }

    @Override
    public List<EmployeSearchResponseDto> search(String matricule, String login, String email, String nom) {
        List<Employe> employes;

        if (matricule != null && !matricule.isBlank()) {
            employes = employeRepository.findByMatricule(matricule).stream().toList();
        } else if (login != null && !login.isBlank()) {
            employes = employeRepository.findByLogin(login).stream().toList();
        } else if (email != null && !email.isBlank()) {
            employes = employeRepository.findByEmail(email).stream().toList();
        } else if (nom != null && !nom.isBlank()) {
            employes = employeRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(nom, nom);
        } else {
            employes = employeRepository.findAll();
        }

        return employes.stream()
                .map(employeMapper::toSearchDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable avec l'id : " + id));

        if (!employe.getProjetsDiriges().isEmpty()) {
            throw new OperationNotAllowedException(
                    "Impossible de supprimer cet employé car il est chef de " +
                    employe.getProjetsDiriges().size() + " projet(s). Réaffectez ces projets d'abord.");
        }

        if (!employe.getAffectations().isEmpty()) {
            throw new OperationNotAllowedException(
                    "Impossible de supprimer cet employé car il a " +
                    employe.getAffectations().size() + " affectation(s) active(s).");
        }

        employeRepository.delete(employe);
    }

    @Override
    public List<EmployeSearchResponseDto> getDisponibles(LocalDate dateDebut, LocalDate dateFin) {
        List<Employe> allEmployes = employeRepository.findAll();

        List<Employe> disponibles = allEmployes.stream()
                .filter(employe -> affectationRepository.findByEmployeId(employe.getId()).stream()
                        .noneMatch(a -> !(dateFin.isBefore(a.getDateDebut()) || dateDebut.isAfter(a.getDateFin()))))
                .toList();

        return disponibles.stream()
                .map(employeMapper::toSearchDto)
                .toList();
    }

    @Override
    public EmployeResponseDto getCurrentUser(String login) {
        Employe employe = employeRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable avec le login : " + login));
        return employeMapper.toResponseDto(employe);
    }

    @Override
    public void changePassword(String login, String oldPassword, String newPassword) {
        Employe employe = employeRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable avec le login : " + login));

        if (!passwordEncoder.matches(oldPassword, employe.getPassword())) {
            throw new OperationNotAllowedException("Ancien mot de passe incorrect");
        }

        employe.setPassword(passwordEncoder.encode(newPassword));
        employeRepository.save(employe);
    }

    private void checkUniqueFields(EmployeRequestDto requestDto, Long currentId) {
        employeRepository.findByMatricule(requestDto.getMatricule())
                .ifPresent(existing -> {
                    if (currentId == null || !existing.getId().equals(currentId)) {
                        throw new DuplicateResourceException("Un employé avec ce matricule existe déjà");
                    }
                });

        employeRepository.findByLogin(requestDto.getLogin())
                .ifPresent(existing -> {
                    if (currentId == null || !existing.getId().equals(currentId)) {
                        throw new DuplicateResourceException("Un employé avec ce login existe déjà");
                    }
                });

        employeRepository.findByEmail(requestDto.getEmail())
                .ifPresent(existing -> {
                    if (currentId == null || !existing.getId().equals(currentId)) {
                        throw new DuplicateResourceException("Un employé avec cet email existe déjà");
                    }
                });
    }
}