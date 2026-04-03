package ma.toubkalit.suiviprojet.config;

import ma.toubkalit.suiviprojet.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataTestConfig {

    @Bean
    CommandLineRunner testRepositories(
            ProjetRepository projetRepository,
            EmployeRepository employeRepository,
            PhaseRepository phaseRepository,
            AffectationRepository affectationRepository
    ) {
        return args -> {
            System.out.println("===== TEST REPOSITORIES =====");

            projetRepository.findByCode("PRJ001")
                    .ifPresent(p -> System.out.println("Projet trouvé : " + p.getNom()));

            employeRepository.findByLogin("alami")
                    .ifPresent(e -> System.out.println("Employé trouvé : " + e.getNom() + " " + e.getPrenom()));

            System.out.println("Phases du projet 1 : " + phaseRepository.findByProjetId(1L).size());
            System.out.println("Affectations employé 1 : " + affectationRepository.findByEmployeId(1L).size());
            System.out.println("Phases terminées non facturées : " + phaseRepository.findByEtatRealisationTrueAndEtatFacturationFalse().size());
            System.out.println("Phases facturées non payées : " + phaseRepository.findByEtatFacturationTrueAndEtatPaiementFalse().size());
        };
    }
}