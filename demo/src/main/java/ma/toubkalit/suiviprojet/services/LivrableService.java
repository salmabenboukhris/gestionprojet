package ma.toubkalit.suiviprojet.services;

import ma.toubkalit.suiviprojet.dto.livrable.LivrableRequestDto;
import ma.toubkalit.suiviprojet.dto.livrable.LivrableResponseDto;

import java.util.List;

public interface LivrableService {

    LivrableResponseDto create(Long phaseId, LivrableRequestDto requestDto);

    List<LivrableResponseDto> getByPhaseId(Long phaseId);

    LivrableResponseDto getById(Long id);

    LivrableResponseDto update(Long id, LivrableRequestDto requestDto);

    void delete(Long id);
}