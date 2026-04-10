package ma.toubkalit.suiviprojet.services.impl;

import ma.toubkalit.suiviprojet.dto.document.DocumentDownloadDto;
import ma.toubkalit.suiviprojet.dto.document.DocumentRequestDto;
import ma.toubkalit.suiviprojet.dto.document.DocumentResponseDto;
import ma.toubkalit.suiviprojet.entities.Document;
import ma.toubkalit.suiviprojet.entities.Projet;
import ma.toubkalit.suiviprojet.exceptions.OperationNotAllowedException;
import ma.toubkalit.suiviprojet.exceptions.ResourceNotFoundException;
import ma.toubkalit.suiviprojet.mappers.DocumentMapper;
import ma.toubkalit.suiviprojet.repositories.DocumentRepository;
import ma.toubkalit.suiviprojet.repositories.ProjetRepository;
import ma.toubkalit.suiviprojet.services.DocumentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final ProjetRepository projetRepository;
    private final DocumentMapper documentMapper;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

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
    public DocumentResponseDto upload(Long projetId, String code, String libelle, MultipartFile file) {
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new ResourceNotFoundException("Projet introuvable avec l'id : " + projetId));

        if (file == null || file.isEmpty()) {
            throw new OperationNotAllowedException("Le fichier est obligatoire");
        }

        try {
            // Create upload directory if not exists
            Path uploadPath = Paths.get(uploadDir, "projets", String.valueOf(projetId));
            Files.createDirectories(uploadPath);

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = (originalFilename != null && originalFilename.contains("."))
                    ? originalFilename.substring(originalFilename.lastIndexOf('.'))
                    : "";
            String storedFilename = UUID.randomUUID() + extension;

            Path filePath = uploadPath.resolve(storedFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Document document = Document.builder()
                    .code(code)
                    .libelle(libelle)
                    .description(originalFilename)
                    .chemin(filePath.toString())
                    .typeFichier(file.getContentType())
                    .taille(file.getSize())
                    .dateUpload(java.time.LocalDateTime.now())
                    .projet(projet)
                    .build();

            Document saved = documentRepository.save(document);
            return documentMapper.toResponseDto(saved);

        } catch (IOException e) {
            throw new OperationNotAllowedException("Erreur lors de l'upload du fichier : " + e.getMessage());
        }
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

        // Optionally delete physical file
        if (document.getChemin() != null) {
            try { Files.deleteIfExists(Paths.get(document.getChemin())); } catch (IOException ignored) {}
        }
        documentRepository.delete(document);
    }

    @Override
    public DocumentDownloadDto download(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document introuvable avec l'id : " + id));

        return documentMapper.toDownloadDto(document);
    }
}