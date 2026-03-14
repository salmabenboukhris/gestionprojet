package ma.toubkalit.services.projetService;

import ma.toubkalit.entity.projet.Document;

import java.util.List;

public interface DocumentService {

    Document saveDocument(Document document);

    Document updateDocument(Integer id, Document document);

    Document getDocumentById(Integer id);

    List<Document> getAllDocuments();

    void deleteDocument(Integer id);
}