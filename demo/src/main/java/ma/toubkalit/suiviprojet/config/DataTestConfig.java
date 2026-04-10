package ma.toubkalit.suiviprojet.config;

import ma.toubkalit.suiviprojet.entities.Employe;
import ma.toubkalit.suiviprojet.entities.Organisme;
import ma.toubkalit.suiviprojet.entities.Profil;
import ma.toubkalit.suiviprojet.entities.Projet;
import ma.toubkalit.suiviprojet.repositories.*;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
public class DataTestConfig {

    @Bean
    CommandLineRunner testRepositories(
            ProfilRepository profilRepository,
            EmployeRepository employeRepository,
            OrganismeRepository organismeRepository,
            ProjetRepository projetRepository,
            PhaseRepository phaseRepository,
            AffectationRepository affectationRepository,
            PasswordEncoder passwordEncoder
    ) {

        return args -> {
            System.out.println("===== SEEDING DATA =====");

            // 1. Seed Profiles
            seedProfil(profilRepository, "ADMIN", "Administrateur Système");
            seedProfil(profilRepository, "SECRETAIRE", "Secrétaire");
            seedProfil(profilRepository, "CHEF_PROJET", "Chef de Projet");
            seedProfil(profilRepository, "COMPTABLE", "Comptable");
            seedProfil(profilRepository, "DIRECTEUR", "Directeur");

            // 2. Seed Employees
            seedEmploye(employeRepository, profilRepository, passwordEncoder, "admin", "admin", "Admin", "admin123", "ADMIN");
            seedEmploye(employeRepository, profilRepository, passwordEncoder, "sec001", "secretaire", "Sec", "sec123", "SECRETAIRE");
            seedEmploye(employeRepository, profilRepository, passwordEncoder, "cp001", "chef_projet", "CP", "cp123", "CHEF_PROJET");
            seedEmploye(employeRepository, profilRepository, passwordEncoder, "comp001", "comptable", "Comp", "comp123", "COMPTABLE");
            seedEmploye(employeRepository, profilRepository, passwordEncoder, "dir001", "directeur", "Dir", "dir123", "DIRECTEUR");

            // 3. Seed Organisme & Projet
            if (organismeRepository.count() == 0) {
                Organisme org = Organisme.builder()
                        .code("ORG001")
                        .nom("Entreprise Test")
                        .nomContact("contact@test.com")
                        .telephone("0522000000")
                        .emailContact("test@test.com")
                        .build();

                organismeRepository.save(org);

                Optional<Employe> chef = employeRepository.findByLogin("admin");
                if (chef.isPresent() && projetRepository.count() == 0) {
                    Projet p = Projet.builder()
                            .code("PRJ001")
                            .nom("Projet de Test Initial")
                            .description("Description du projet de test")
                            .dateDebut(java.time.LocalDate.now())
                            .dateFin(java.time.LocalDate.now().plusMonths(6))
                            .montant(new java.math.BigDecimal("100000"))
                            .organisme(org)
                            .chefProjet(chef.get())
                            .build();
                    projetRepository.save(p);
                }
            }

            System.out.println("===== SEEDING COMPLETED =====");
        };
    }


    private void seedProfil(ProfilRepository repo, String code, String libelle) {
        if (!repo.existsByCode(code)) {
            repo.save(Profil.builder().code(code).libelle(libelle).build());
        }
    }

    private void seedEmploye(EmployeRepository empRepo, ProfilRepository profRepo, PasswordEncoder encoder,
                             String matricule, String login, String nom, String password, String profilCode) {
        if (!empRepo.existsByLogin(login)) {
            Optional<Profil> profil = profRepo.findByCode(profilCode);
            if (profil.isPresent()) {
                Employe employe = Employe.builder()
                        .matricule(matricule)
                        .login(login)
                        .nom(nom)
                        .prenom(login)
                        .email(login + "@toubkal-it.ma")
                        .password(encoder.encode(password))
                        .profil(profil.get())
                        .build();
                empRepo.save(employe);
            }
        }
    }
}