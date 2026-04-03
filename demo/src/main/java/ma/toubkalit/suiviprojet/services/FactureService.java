package ma.toubkalit.suiviprojet.services;

import ma.toubkalit.suiviprojet.dto.facture.FactureRequestDto;
import ma.toubkalit.suiviprojet.dto.facture.FactureResponseDto;

import java.util.List;

public interface FactureService {

    FactureResponseDto create(Long phaseId, FactureRequestDto requestDto);

    List<FactureResponseDto> getAll();

    FactureResponseDto getById(Long id);

    FactureResponseDto update(Long id, FactureRequestDto requestDto);

    void delete(Long id);
}