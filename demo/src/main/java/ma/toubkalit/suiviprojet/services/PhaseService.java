package ma.toubkalit.suiviprojet.services;

import ma.toubkalit.suiviprojet.dto.phase.PhaseRequestDto;
import ma.toubkalit.suiviprojet.dto.phase.PhaseResponseDto;

import java.util.List;

public interface PhaseService {

    PhaseResponseDto create(Long projetId, PhaseRequestDto requestDto);

    List<PhaseResponseDto> getAll();

    List<PhaseResponseDto> getByProjetId(Long projetId);

    PhaseResponseDto getById(Long id);

    PhaseResponseDto update(Long id, PhaseRequestDto requestDto);

    void delete(Long id);

    PhaseResponseDto updateEtatRealisation(Long id, Boolean value);

    PhaseResponseDto updateEtatFacturation(Long id, Boolean value);

    PhaseResponseDto updateEtatPaiement(Long id, Boolean value);
}