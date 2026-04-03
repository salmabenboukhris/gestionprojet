package ma.toubkalit.suiviprojet.services.impl;

import ma.toubkalit.suiviprojet.dto.employe.EmployeRequestDto;
import ma.toubkalit.suiviprojet.dto.employe.EmployeResponseDto;
import ma.toubkalit.suiviprojet.dto.employe.EmployeSearchResponseDto;
import ma.toubkalit.suiviprojet.entities.Employe;
import ma.toubkalit.suiviprojet.entities.Profil;
import ma.toubkalit.suiviprojet.exceptions.DuplicateResourceException;
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

    public EmployeServiceImpl(EmployeRepository employeRepository,
                              ProfilRepository profilRepository,
                              AffectationRepository affectationRepository,
                              EmployeMapper employeMapper) {
        this.employeRepository = employeRepository;
        this.profilRepository = profilRepository;
        this.affectationRepository = affectationRepository;
        this.employeMapper = employeMapper;
    }

    @Override
    public EmployeResponseDto create(EmployeRequestDto requestDto) {
        checkUniqueFields(requestDto, null);

        Profil profil = profilRepository.findById(requestDto.getProfilId())
                .orElseThrow(() -> new ResourceNotFoundException("Profil introuvable avec l'id : " + requestDto.getProfilId()));

        Employe employe = employeMapper.toEntity(requestDto, profil);
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

        employeMapper.updateEntity(employe, requestDto, profil);
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