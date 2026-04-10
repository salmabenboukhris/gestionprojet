package ma.toubkalit.suiviprojet.services;

import ma.toubkalit.suiviprojet.dto.document.DocumentDownloadDto;
import ma.toubkalit.suiviprojet.dto.document.DocumentRequestDto;
import ma.toubkalit.suiviprojet.dto.document.DocumentResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {

    DocumentResponseDto create(Long projetId, DocumentRequestDto requestDto);

    DocumentResponseDto upload(Long projetId, String code, String libelle, MultipartFile file);

    List<DocumentResponseDto> getByProjetId(Long projetId);

    DocumentResponseDto getById(Long id);

    DocumentResponseDto update(Long id, DocumentRequestDto requestDto);

    void delete(Long id);

    DocumentDownloadDto download(Long id);
}