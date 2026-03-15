package ma.toubkalit.config;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.entity.organisation.Employe;
import ma.toubkalit.entity.organisation.Profil;
import ma.toubkalit.enums.RoleCode;
import ma.toubkalit.repositories.organisationRepo.EmployeRepo;
import ma.toubkalit.repositories.organisationRepo.ProfilRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProfilRepo profilRepo;
    private final EmployeRepo employeRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Init Profils
        for (RoleCode role : RoleCode.values()) {
            if (profilRepo.findByRoleCode(role).isEmpty()) {
                Profil profil = Profil.builder()
                        .roleCode(role)
                        .code(role.name())
                        .libelle("Profil " + role.name())
                        .build();
                profilRepo.save(profil);
            }
        }

        // Init Admin User
        Profil adminProfil = profilRepo.findByRoleCode(RoleCode.ADMIN)
                .orElseThrow(() -> new RuntimeException("Profil ADMIN non trouvé"));

        employeRepo.findByLogin("admin").ifPresentOrElse(
            admin -> {
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setProfil(adminProfil);
                employeRepo.save(admin);
                System.out.println(">>> Compte Administrateur 'admin' mis à jour (PWD + Profil ADMIN)");
            },
            () -> {
                Employe admin = Employe.builder()
                        .matricule("ADM001")
                        .nom("ADMIN")
                        .prenom("System")
                        .email("admin@toubkalit.ma")
                        .login("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .profil(adminProfil)
                        .build();
                
                employeRepo.save(admin);
                System.out.println(">>> Compte Administrateur par défaut créé : admin / admin123");
            }
        );
    }
}
