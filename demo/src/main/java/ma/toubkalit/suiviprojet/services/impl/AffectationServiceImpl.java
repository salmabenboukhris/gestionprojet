package ma.toubkalit.suiviprojet.services.impl;

import ma.toubkalit.suiviprojet.dto.affectation.AffectationRequestDto;
import ma.toubkalit.suiviprojet.dto.affectation.AffectationResponseDto;
import ma.toubkalit.suiviprojet.entities.Affectation;
import ma.toubkalit.suiviprojet.entities.Employe;
import ma.toubkalit.suiviprojet.entities.Phase;
import ma.toubkalit.suiviprojet.exceptions.OperationNotAllowedException;
import ma.toubkalit.suiviprojet.exceptions.ResourceNotFoundException;
import ma.toubkalit.suiviprojet.mappers.AffectationMapper;
import ma.toubkalit.suiviprojet.repositories.AffectationRepository;
import ma.toubkalit.suiviprojet.repositories.EmployeRepository;
import ma.toubkalit.suiviprojet.repositories.PhaseRepository;
import ma.toubkalit.suiviprojet.services.AffectationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AffectationServiceImpl implements AffectationService {

    private final AffectationRepository affectationRepository;
    private final EmployeRepository employeRepository;
    private final PhaseRepository phaseRepository;
    private final AffectationMapper affectationMapper;

    public AffectationServiceImpl(AffectationRepository affectationRepository,
                                  EmployeRepository employeRepository,
                                  PhaseRepository phaseRepository,
                                  AffectationMapper affectationMapper) {
        this.affectationRepository = affectationRepository;
        this.employeRepository = employeRepository;
        this.phaseRepository = phaseRepository;
        this.affectationMapper = affectationMapper;
    }

    @Override
    public AffectationResponseDto create(Long phaseId, Long employeId, AffectationRequestDto requestDto) {
        Phase phase = phaseRepository.findById(phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Phase introuvable avec l'id : " + phaseId));

        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable avec l'id : " + employeId));

        if (affectationRepository.existsByEmployeIdAndPhaseId(employeId, phaseId)) {
            throw new OperationNotAllowedException("Cette affectation existe déjà");
        }

        validateAffectation(requestDto, phase, employeId, null);

        Affectation affectation = affectationMapper.toEntity(requestDto, employe, phase);
        Affectation saved = affectationRepository.save(affectation);
        return affectationMapper.toResponseDto(saved);
    }

    @Override
    public List<AffectationResponseDto> getEmployesByPhase(Long phaseId) {
        phaseRepository.findById(phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Phase introuvable avec l'id : " + phaseId));

        return affectationRepository.findByPhaseId(phaseId).stream()
                .map(affectationMapper::toResponseDto)
                .toList();
    }

    @Override
    public AffectationResponseDto getByPhaseAndEmploye(Long phaseId, Long employeId) {
        Affectation affectation = affectationRepository.findByEmployeIdAndPhaseId(employeId, phaseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Affectation introuvable pour phaseId=" + phaseId + " et employeId=" + employeId));

        return affectationMapper.toResponseDto(affectation);
    }

    @Override
    public AffectationResponseDto update(Long phaseId, Long employeId, AffectationRequestDto requestDto) {
        Affectation affectation = affectationRepository.findByEmployeIdAndPhaseId(employeId, phaseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Affectation introuvable pour phaseId=" + phaseId + " et employeId=" + employeId));

        validateAffectation(requestDto, affectation.getPhase(), employeId, phaseId);

        affectationMapper.updateEntity(affectation, requestDto);
        Affectation updated = affectationRepository.save(affectation);
        return affectationMapper.toResponseDto(updated);
    }

    @Override
    public void delete(Long phaseId, Long employeId) {
        Affectation affectation = affectationRepository.findByEmployeIdAndPhaseId(employeId, phaseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Affectation introuvable pour phaseId=" + phaseId + " et employeId=" + employeId));

        affectationRepository.delete(affectation);
    }

    @Override
    public List<AffectationResponseDto> getPhasesByEmploye(Long employeId) {
        employeRepository.findById(employeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable avec l'id : " + employeId));

        return affectationRepository.findByEmployeId(employeId).stream()
                .map(affectationMapper::toResponseDto)
                .toList();
    }

    private void validateAffectation(AffectationRequestDto requestDto, Phase phase, Long employeId, Long currentPhaseId) {
        if (requestDto.getDateDebut().isAfter(requestDto.getDateFin())) {
            throw new OperationNotAllowedException("La date de début doit être inférieure ou égale à la date de fin");
        }

        if (requestDto.getDateDebut().isBefore(phase.getDateDebut()) ||
                requestDto.getDateFin().isAfter(phase.getDateFin())) {
            throw new OperationNotAllowedException("Les dates d'affectation doivent être incluses dans la période de la phase");
        }

        boolean indisponible = affectationRepository.findByEmployeId(employeId).stream()
                .filter(existing -> currentPhaseId == null || !existing.getPhase().getId().equals(currentPhaseId))
                .anyMatch(existing ->
                        !(requestDto.getDateFin().isBefore(existing.getDateDebut()) ||
                                requestDto.getDateDebut().isAfter(existing.getDateFin()))
                );

        if (indisponible) {
            throw new OperationNotAllowedException("L'employé n'est pas disponible sur cette période");
        }
    }
}