package ma.toubkalit.repositories.projetRepo;

import ma.toubkalit.entity.projet.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepo extends JpaRepository<Document, Integer> {

}