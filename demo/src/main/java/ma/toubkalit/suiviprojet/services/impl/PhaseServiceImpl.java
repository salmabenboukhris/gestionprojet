package ma.toubkalit.suiviprojet.services.impl;

import ma.toubkalit.suiviprojet.dto.phase.PhaseRequestDto;
import ma.toubkalit.suiviprojet.dto.phase.PhaseResponseDto;
import ma.toubkalit.suiviprojet.entities.Phase;
import ma.toubkalit.suiviprojet.entities.Projet;
import ma.toubkalit.suiviprojet.exceptions.OperationNotAllowedException;
import ma.toubkalit.suiviprojet.exceptions.ResourceNotFoundException;
import ma.toubkalit.suiviprojet.mappers.PhaseMapper;
import ma.toubkalit.suiviprojet.repositories.PhaseRepository;
import ma.toubkalit.suiviprojet.repositories.ProjetRepository;
import ma.toubkalit.suiviprojet.services.PhaseService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PhaseServiceImpl implements PhaseService {

    private final PhaseRepository phaseRepository;
    private final ProjetRepository projetRepository;
    private final PhaseMapper phaseMapper;

    public PhaseServiceImpl(PhaseRepository phaseRepository,
                            ProjetRepository projetRepository,
                            PhaseMapper phaseMapper) {
        this.phaseRepository = phaseRepository;
        this.projetRepository = projetRepository;
        this.phaseMapper = phaseMapper;
    }

    @Override
    public PhaseResponseDto create(Long projetId, PhaseRequestDto requestDto) {
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable avec l'id : " + projetId));

        validatePhase(requestDto, projet, null);

        Phase phase = phaseMapper.toEntity(requestDto, projet);
        Phase saved = phaseRepository.save(phase);
        return phaseMapper.toResponseDto(saved);
    }

    @Override
    public List<PhaseResponseDto> getByProjetId(Long projetId) {
        projetRepository.findById(projetId)
                .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable avec l'id : " + projetId));

        return phaseRepository.findByProjetId(projetId).stream()
                .map(phaseMapper::toResponseDto)
                .toList();
    }

    @Override
    public PhaseResponseDto getById(Long id) {
        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Phase introuvable avec l'id : " + id));

        return phaseMapper.toResponseDto(phase);
    }

    @Override
    public PhaseResponseDto update(Long id, PhaseRequestDto requestDto) {
        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Phase introuvable avec l'id : " + id));

        Projet projet = phase.getProjet();

        validatePhase(requestDto, projet, id);

        phaseMapper.updateEntity(phase, requestDto, projet);
        Phase updated = phaseRepository.save(phase);
        return phaseMapper.toResponseDto(updated);
    }

    @Override
    public void delete(Long id) {
        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Phase introuvable avec l'id : " + id));

        if (phase.getFacture() != null) {
            throw new OperationNotAllowedException("Impossible de supprimer cette phase car elle est déjà liée à une facture");
        }

        if (phase.getLivrables() != null && !phase.getLivrables().isEmpty()) {
            throw new OperationNotAllowedException("Impossible de supprimer cette phase car elle contient des livrables");
        }

        if (phase.getAffectations() != null && !phase.getAffectations().isEmpty()) {
            throw new OperationNotAllowedException("Impossible de supprimer cette phase car elle contient des affectations");
        }

        phaseRepository.delete(phase);
    }

    @Override
    public PhaseResponseDto updateEtatRealisation(Long id, Boolean value) {
        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Phase introuvable avec l'id : " + id));

        phase.setEtatRealisation(value);
        return phaseMapper.toResponseDto(phaseRepository.save(phase));
    }

    @Override
    public PhaseResponseDto updateEtatFacturation(Long id, Boolean value) {
        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Phase introuvable avec l'id : " + id));

        if (Boolean.TRUE.equals(value) && !Boolean.TRUE.equals(phase.getEtatRealisation())) {
            throw new OperationNotAllowedException("Une phase ne peut pas être facturée si elle n'est pas terminée");
        }

        phase.setEtatFacturation(value);

        if (Boolean.FALSE.equals(value)) {
            phase.setEtatPaiement(false);
        }

        return phaseMapper.toResponseDto(phaseRepository.save(phase));
    }

    @Override
    public PhaseResponseDto updateEtatPaiement(Long id, Boolean value) {
        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Phase introuvable avec l'id : " + id));

        if (Boolean.TRUE.equals(value) && !Boolean.TRUE.equals(phase.getEtatFacturation())) {
            throw new OperationNotAllowedException("Une phase ne peut pas être payée si elle n'est pas facturée");
        }

        phase.setEtatPaiement(value);
        return phaseMapper.toResponseDto(phaseRepository.save(phase));
    }

    private void validatePhase(PhaseRequestDto requestDto, Projet projet, Long currentPhaseId) {

        if (requestDto.getDateDebut().isAfter(requestDto.getDateFin())) {
            throw new OperationNotAllowedException("La date de début doit être inférieure ou égale à la date de fin");
        }

        if (requestDto.getDateDebut().isBefore(projet.getDateDebut()) ||
                requestDto.getDateFin().isAfter(projet.getDateFin())) {
            throw new OperationNotAllowedException("Les dates de la phase doivent être incluses dans l'intervalle du projet");
        }

        BigDecimal sommeAutresPhases = phaseRepository.findByProjetId(projet.getId()).stream()
                .filter(existing -> currentPhaseId == null || !existing.getId().equals(currentPhaseId))
                .map(Phase::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal sommeTotale = sommeAutresPhases.add(requestDto.getMontant());

        if (sommeTotale.compareTo(projet.getMontant()) > 0) {
            throw new OperationNotAllowedException("La somme des montants des phases ne doit pas dépasser le montant du projet");
        }
    }
}