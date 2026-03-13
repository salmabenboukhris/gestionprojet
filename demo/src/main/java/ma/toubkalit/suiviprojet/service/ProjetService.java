package ma.toubkalit.suiviprojet.service;

import ma.toubkalit.suiviprojet.projet.Projet;
import ma.toubkalit.suiviprojet.repositories.ProjetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjetService {

    @Autowired
    ProjetRepository projetRepository;

    public List<Projet> getAll() {
        return projetRepository.findAll();
    }

    public Projet save(Projet projet) {
        return projetRepository.save(projet);
    }
}