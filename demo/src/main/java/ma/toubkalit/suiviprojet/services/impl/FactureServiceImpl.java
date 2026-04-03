package ma.toubkalit.suiviprojet.services.impl;

import ma.toubkalit.suiviprojet.dto.facture.FactureRequestDto;
import ma.toubkalit.suiviprojet.dto.facture.FactureResponseDto;
import ma.toubkalit.suiviprojet.entities.Facture;
import ma.toubkalit.suiviprojet.entities.Phase;
import ma.toubkalit.suiviprojet.exceptions.DuplicateResourceException;
import ma.toubkalit.suiviprojet.exceptions.OperationNotAllowedException;
import ma.toubkalit.suiviprojet.exceptions.ResourceNotFoundException;
import ma.toubkalit.suiviprojet.mappers.FactureMapper;
import ma.toubkalit.suiviprojet.repositories.FactureRepository;
import ma.toubkalit.suiviprojet.repositories.PhaseRepository;
import ma.toubkalit.suiviprojet.services.FactureService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FactureServiceImpl implements FactureService {

    private final FactureRepository factureRepository;
    private final PhaseRepository phaseRepository;
    private final FactureMapper factureMapper;

    public FactureServiceImpl(FactureRepository factureRepository,
                              PhaseRepository phaseRepository,
                              FactureMapper factureMapper) {
        this.factureRepository = factureRepository;
        this.phaseRepository = phaseRepository;
        this.factureMapper = factureMapper;
    }

    @Override
    public FactureResponseDto create(Long phaseId, FactureRequestDto requestDto) {
        Phase phase = phaseRepository.findById(phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Phase introuvable avec l'id : " + phaseId));

        validateCreateFacture(phase, requestDto);

        Facture facture = factureMapper.toEntity(requestDto, phase);
        Facture saved = factureRepository.save(facture);

        phase.setEtatFacturation(true);
        phaseRepository.save(phase);

        return factureMapper.toResponseDto(saved);
    }

    @Override
    public List<FactureResponseDto> getAll() {
        return factureRepository.findAll().stream()
                .map(factureMapper::toResponseDto)
                .toList();
    }

    @Override
    public FactureResponseDto getById(Long id) {
        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facture introuvable avec l'id : " + id));

        return factureMapper.toResponseDto(facture);
    }

    @Override
    public FactureResponseDto update(Long id, FactureRequestDto requestDto) {
        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facture introuvable avec l'id : " + id));

        factureRepository.findByCode(requestDto.getCode())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new DuplicateResourceException("Une autre facture avec ce code existe déjà");
                    }
                });

        factureMapper.updateEntity(facture, requestDto);
        Facture updated = factureRepository.save(facture);
        return factureMapper.toResponseDto(updated);
    }

    @Override
    public void delete(Long id) {
        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facture introuvable avec l'id : " + id));

        Phase phase = facture.getPhase();

        if (Boolean.TRUE.equals(phase.getEtatPaiement())) {
            throw new OperationNotAllowedException("Impossible de supprimer une facture d'une phase déjà payée");
        }

        factureRepository.delete(facture);

        phase.setEtatFacturation(false);
        phaseRepository.save(phase);
    }

    private void validateCreateFacture(Phase phase, FactureRequestDto requestDto) {
        if (!Boolean.TRUE.equals(phase.getEtatRealisation())) {
            throw new OperationNotAllowedException("Une facture doit concerner une phase terminée");
        }

        if (factureRepository.existsByPhaseId(phase.getId())) {
            throw new OperationNotAllowedException("Cette phase a déjà une facture");
        }

        if (factureRepository.existsByCode(requestDto.getCode())) {
            throw new DuplicateResourceException("Une facture avec ce code existe déjà");
        }
    }
}