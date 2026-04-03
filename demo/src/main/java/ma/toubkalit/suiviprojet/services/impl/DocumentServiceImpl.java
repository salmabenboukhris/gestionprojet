package ma.toubkalit.suiviprojet.services.impl;

import ma.toubkalit.suiviprojet.dto.document.DocumentDownloadDto;
import ma.toubkalit.suiviprojet.dto.document.DocumentRequestDto;
import ma.toubkalit.suiviprojet.dto.document.DocumentResponseDto;
import ma.toubkalit.suiviprojet.entities.Document;
import ma.toubkalit.suiviprojet.entities.Projet;
import ma.toubkalit.suiviprojet.exceptions.ResourceNotFoundException;
import ma.toubkalit.suiviprojet.mappers.DocumentMapper;
import ma.toubkalit.suiviprojet.repositories.DocumentRepository;
import ma.toubkalit.suiviprojet.repositories.ProjetRepository;
import ma.toubkalit.suiviprojet.services.DocumentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final ProjetRepository projetRepository;
    private final DocumentMapper documentMapper;

    public DocumentServiceImpl(DocumentRepository documentRepository,
                               ProjetRepository projetRepository,
                               DocumentMapper documentMapper) {
        this.documentRepository = documentRepository;
        this.projetRepository = projetRepository;
        this.documentMapper = documentMapper;
    }

    @Override
    public DocumentResponseDto create(Long projetId, DocumentRequestDto requestDto) {
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable avec l'id : " + projetId));

        Document document = documentMapper.toEntity(requestDto, projet);
        Document saved = documentRepository.save(document);
        return documentMapper.toResponseDto(saved);
    }

    @Override
    public List<DocumentResponseDto> getByProjetId(Long projetId) {
        projetRepository.findById(projetId)
                .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable avec l'id : " + projetId));

        return documentRepository.findByProjetId(projetId).stream()
                .map(documentMapper::toResponseDto)
                .toList();
    }

    @Override
    public DocumentResponseDto getById(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document introuvable avec l'id : " + id));

        return documentMapper.toResponseDto(document);
    }

    @Override
    public DocumentResponseDto update(Long id, DocumentRequestDto requestDto) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document introuvable avec l'id : " + id));

        documentMapper.updateEntity(document, requestDto);
        Document updated = documentRepository.save(document);
        return documentMapper.toResponseDto(updated);
    }

    @Override
    public void delete(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document introuvable avec l'id : " + id));

        documentRepository.delete(document);
    }

    @Override
    public DocumentDownloadDto download(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document introuvable avec l'id : " + id));

        return documentMapper.toDownloadDto(document);
    }
}