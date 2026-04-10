package ma.toubkalit.suiviprojet.services;

import ma.toubkalit.suiviprojet.dto.affectation.AffectationRequestDto;
import ma.toubkalit.suiviprojet.dto.affectation.AffectationResponseDto;

import java.util.List;

public interface AffectationService {

    AffectationResponseDto create(Long phaseId, Long employeId, AffectationRequestDto requestDto);

    List<AffectationResponseDto> getEmployesByPhase(Long phaseId);

    AffectationResponseDto getByPhaseAndEmploye(Long phaseId, Long employeId);

    AffectationResponseDto update(Long phaseId, Long employeId, AffectationRequestDto requestDto);

    void delete(Long phaseId, Long employeId);

    List<AffectationResponseDto> getPhasesByEmploye(Long employeId);
}