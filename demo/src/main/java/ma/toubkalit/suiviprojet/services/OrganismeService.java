package ma.toubkalit.suiviprojet.services;

import ma.toubkalit.suiviprojet.dto.organisme.OrganismeRequestDto;
import ma.toubkalit.suiviprojet.dto.organisme.OrganismeResponseDto;

import java.util.List;

public interface OrganismeService {

    OrganismeResponseDto create(OrganismeRequestDto requestDto);

    OrganismeResponseDto update(Long id, OrganismeRequestDto requestDto);

    OrganismeResponseDto getById(Long id);

    List<OrganismeResponseDto> getAll(String code, String nom, String contact);

    void delete(Long id);
}