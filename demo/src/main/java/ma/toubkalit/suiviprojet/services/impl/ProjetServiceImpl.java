package ma.toubkalit.suiviprojet.services.impl;

import ma.toubkalit.suiviprojet.dto.projet.ProjetRequestDto;
import ma.toubkalit.suiviprojet.dto.projet.ProjetResponseDto;
import ma.toubkalit.suiviprojet.dto.projet.ProjetResumeDto;
import ma.toubkalit.suiviprojet.entities.Employe;
import ma.toubkalit.suiviprojet.entities.Organisme;
import ma.toubkalit.suiviprojet.entities.Projet;
import ma.toubkalit.suiviprojet.exceptions.DuplicateResourceException;
import ma.toubkalit.suiviprojet.exceptions.OperationNotAllowedException;
import ma.toubkalit.suiviprojet.exceptions.ResourceNotFoundException;
import ma.toubkalit.suiviprojet.mappers.ProjetMapper;
import ma.toubkalit.suiviprojet.repositories.EmployeRepository;
import ma.toubkalit.suiviprojet.repositories.OrganismeRepository;
import ma.toubkalit.suiviprojet.repositories.ProjetRepository;
import ma.toubkalit.suiviprojet.services.ProjetService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjetServiceImpl implements ProjetService {

    private final ProjetRepository projetRepository;
    private final OrganismeRepository organismeRepository;
    private final EmployeRepository employeRepository;
    private final ProjetMapper projetMapper;

    public ProjetServiceImpl(ProjetRepository projetRepository,
                             OrganismeRepository organismeRepository,
                             EmployeRepository employeRepository,
                             ProjetMapper projetMapper) {
        this.projetRepository = projetRepository;
        this.organismeRepository = organismeRepository;
        this.employeRepository = employeRepository;
        this.projetMapper = projetMapper;
    }

    @Override
    public ProjetResponseDto create(ProjetRequestDto requestDto) {
        validateProjet(requestDto, null);

        Organisme organisme = organismeRepository.findById(requestDto.getOrganismeId())
                .orElseThrow(() -> new ResourceNotFoundException("Organisme introuvable avec l'id : " + requestDto.getOrganismeId()));

        Employe chefProjet = employeRepository.findById(requestDto.getChefProjetId())
                .orElseThrow(() -> new ResourceNotFoundException("Chef de projet introuvable avec l'id : " + requestDto.getChefProjetId()));

        Projet projet = projetMapper.toEntity(requestDto, organisme, chefProjet);
        Projet saved = projetRepository.save(projet);
        return projetMapper.toResponseDto(saved);
    }

    @Override
    public ProjetResponseDto update(Long id, ProjetRequestDto requestDto) {
        Projet projet = projetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable avec l'id : " + id));

        validateProjet(requestDto, id);

        Organisme organisme = organismeRepository.findById(requestDto.getOrganismeId())
                .orElseThrow(() -> new ResourceNotFoundException("Organisme introuvable avec l'id : " + requestDto.getOrganismeId()));

        Employe chefProjet = employeRepository.findById(requestDto.getChefProjetId())
                .orElseThrow(() -> new ResourceNotFoundException("Chef de projet introuvable avec l'id : " + requestDto.getChefProjetId()));

        projetMapper.updateEntity(projet, requestDto, organisme, chefProjet);
        Projet updated = projetRepository.save(projet);
        return projetMapper.toResponseDto(updated);
    }

    @Override
    public ProjetResponseDto getById(Long id) {
        Projet projet = projetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable avec l'id : " + id));

        return projetMapper.toResponseDto(projet);
    }

    @Override
    public List<ProjetResponseDto> getAll(String code, String nom, Long organismeId, Long chefProjetId) {
        List<Projet> projets;

        if (code != null && !code.isBlank()) {
            projets = projetRepository.findByCode(code).stream().toList();
        } else if (nom != null && !nom.isBlank()) {
            projets = projetRepository.findByNomContainingIgnoreCase(nom);
        } else if (organismeId != null) {
            projets = projetRepository.findByOrganismeId(organismeId);
        } else if (chefProjetId != null) {
            projets = projetRepository.findByChefProjetId(chefProjetId);
        } else {
            projets = projetRepository.findAll();
        }

        return projets.stream()
                .map(projetMapper::toResponseDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        Projet projet = projetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable avec l'id : " + id));

        if (projet.getPhases() != null && !projet.getPhases().isEmpty()) {
            throw new OperationNotAllowedException("Impossible de supprimer ce projet car il contient des phases");
        }

        if (projet.getDocuments() != null && !projet.getDocuments().isEmpty()) {
            throw new OperationNotAllowedException("Impossible de supprimer ce projet car il contient des documents");
        }

        projetRepository.delete(projet);
    }

    @Override
    public ProjetResumeDto getResume(Long id) {
        Projet projet = projetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable avec l'id : " + id));

        return projetMapper.toResumeDto(projet);
    }

    private void validateProjet(ProjetRequestDto requestDto, Long currentId) {
        if (requestDto.getDateDebut().isAfter(requestDto.getDateFin())) {
            throw new OperationNotAllowedException("Date début > date fin");
        }

        projetRepository.findByCode(requestDto.getCode())
                .ifPresent(existing -> {
                    if (currentId == null || !existing.getId().equals(currentId)) {
                        throw new DuplicateResourceException("Un projet avec ce code existe déjà");
                    }
                });
    }
}