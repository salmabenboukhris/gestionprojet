package ma.toubkalit.suiviprojet.services.impl;

import ma.toubkalit.suiviprojet.dto.organisme.OrganismeRequestDto;
import ma.toubkalit.suiviprojet.dto.organisme.OrganismeResponseDto;
import ma.toubkalit.suiviprojet.entities.Organisme;
import ma.toubkalit.suiviprojet.exceptions.DuplicateResourceException;
import ma.toubkalit.suiviprojet.exceptions.OperationNotAllowedException;
import ma.toubkalit.suiviprojet.exceptions.ResourceNotFoundException;
import ma.toubkalit.suiviprojet.mappers.OrganismeMapper;
import ma.toubkalit.suiviprojet.repositories.OrganismeRepository;
import ma.toubkalit.suiviprojet.repositories.ProjetRepository;
import ma.toubkalit.suiviprojet.services.OrganismeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrganismeServiceImpl implements OrganismeService {

    private final OrganismeRepository organismeRepository;
    private final OrganismeMapper organismeMapper;
    private final ProjetRepository projetRepository;

    public OrganismeServiceImpl(OrganismeRepository organismeRepository,
                                OrganismeMapper organismeMapper,
                                ProjetRepository projetRepository) {
        this.organismeRepository = organismeRepository;
        this.organismeMapper = organismeMapper;
        this.projetRepository = projetRepository;
    }

    @Override
    public OrganismeResponseDto create(OrganismeRequestDto requestDto) {
        if (organismeRepository.existsByCode(requestDto.getCode())) {
            throw new DuplicateResourceException("Un organisme avec ce code existe déjà");
        }

        Organisme organisme = organismeMapper.toEntity(requestDto);
        Organisme saved = organismeRepository.save(organisme);
        return organismeMapper.toResponseDto(saved);
    }

    @Override
    public OrganismeResponseDto update(Long id, OrganismeRequestDto requestDto) {
        Organisme organisme = organismeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organisme introuvable avec l'id : " + id));

        organismeRepository.findByCode(requestDto.getCode())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new DuplicateResourceException("Un autre organisme avec ce code existe déjà");
                    }
                });

        organismeMapper.updateEntityFromDto(requestDto, organisme);
        Organisme updated = organismeRepository.save(organisme);
        return organismeMapper.toResponseDto(updated);
    }

    @Override
    public OrganismeResponseDto getById(Long id) {
        Organisme organisme = organismeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organisme introuvable avec l'id : " + id));

        return organismeMapper.toResponseDto(organisme);
    }

    @Override
    public List<OrganismeResponseDto> getAll(String code, String nom, String contact) {
        List<Organisme> result = new ArrayList<>();

        if (code != null && !code.isBlank()) {
            organismeRepository.findByCode(code).ifPresent(result::add);
        } else if (nom != null && !nom.isBlank()) {
            result = organismeRepository.findByNomContainingIgnoreCase(nom);
        } else if (contact != null && !contact.isBlank()) {
            result = organismeRepository.findByNomContactContainingIgnoreCase(contact);
        } else {
            result = organismeRepository.findAll();
        }

        return result.stream()
                .map(organismeMapper::toResponseDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        Organisme organisme = organismeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organisme introuvable avec l'id : " + id));

        if (projetRepository.existsByOrganismeId(id)) {
            throw new OperationNotAllowedException("Impossible de supprimer cet organisme car il est lié à un ou plusieurs projets");
        }

        organismeRepository.delete(organisme);
    }
}