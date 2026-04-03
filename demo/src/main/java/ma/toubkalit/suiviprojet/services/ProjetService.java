package ma.toubkalit.suiviprojet.services;

import ma.toubkalit.suiviprojet.dto.projet.ProjetRequestDto;
import ma.toubkalit.suiviprojet.dto.projet.ProjetResponseDto;
import ma.toubkalit.suiviprojet.dto.projet.ProjetResumeDto;

import java.util.List;

public interface ProjetService {

    ProjetResponseDto create(ProjetRequestDto requestDto);

    ProjetResponseDto update(Long id, ProjetRequestDto requestDto);

    ProjetResponseDto getById(Long id);

    List<ProjetResponseDto> getAll(String code, String nom, Long organismeId, Long chefProjetId);

    void delete(Long id);

    ProjetResumeDto getResume(Long id);
}