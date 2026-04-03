package ma.toubkalit.suiviprojet.services;

import ma.toubkalit.suiviprojet.dto.document.DocumentDownloadDto;
import ma.toubkalit.suiviprojet.dto.document.DocumentRequestDto;
import ma.toubkalit.suiviprojet.dto.document.DocumentResponseDto;

import java.util.List;

public interface DocumentService {

    DocumentResponseDto create(Long projetId, DocumentRequestDto requestDto);

    List<DocumentResponseDto> getByProjetId(Long projetId);

    DocumentResponseDto getById(Long id);

    DocumentResponseDto update(Long id, DocumentRequestDto requestDto);

    void delete(Long id);

    DocumentDownloadDto download(Long id);
}