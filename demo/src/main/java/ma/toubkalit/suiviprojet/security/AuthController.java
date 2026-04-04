package ma.toubkalit.suiviprojet.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.toubkalit.suiviprojet.dto.auth.ChangePasswordRequest;
import ma.toubkalit.suiviprojet.dto.auth.LoginRequest;
import ma.toubkalit.suiviprojet.dto.employe.EmployeResponseDto;
import ma.toubkalit.suiviprojet.services.EmployeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "Gestion de la connexion et du profil utilisateur")
public class AuthController {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final EmployeService employeService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @Operation(summary = "Se connecter pour obtenir un jeton JWT")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {

        if (request.getLogin() == null || request.getPassword() == null) {
            throw new RuntimeException("Login ou mot de passe manquant");
        }

        UserDetails user = userDetailsService.loadUserByUsername(request.getLogin());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        String token = jwtService.generateToken(user.getUsername());

        return ResponseEntity.ok(token);
    }

    @GetMapping("/me")
    public ResponseEntity<EmployeResponseDto> getCurrentUser(Principal principal) {
        return ResponseEntity.ok(employeService.getCurrentUser(principal.getName()));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(Principal principal, @Valid @RequestBody ChangePasswordRequest request) {
        employeService.changePassword(principal.getName(), request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }
}