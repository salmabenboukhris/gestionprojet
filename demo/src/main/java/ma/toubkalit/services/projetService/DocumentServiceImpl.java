package ma.toubkalit.services.projetService;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.entity.projet.Document;
import ma.toubkalit.repositories.projetRepo.DocumentRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepo documentRepo;

    @Override
    public Document saveDocument(Document document) {
        return documentRepo.save(document);
    }

    @Override
    public Document updateDocument(Integer id, Document document) {
        Document existing = getDocumentById(id);
        existing.setCode(document.getCode());
        existing.setLibelle(document.getLibelle());
        existing.setDescription(document.getDescription());
        existing.setChemin(document.getChemin());
        existing.setProjet(document.getProjet());
        return documentRepo.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public Document getDocumentById(Integer id) {
        return documentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Document introuvable avec l'id : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Document> getAllDocuments() {
        return documentRepo.findAll();
    }

    @Override
    public void deleteDocument(Integer id) {
        Document document = getDocumentById(id);
        documentRepo.delete(document);
    }
}