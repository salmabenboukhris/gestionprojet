package ma.toubkalit.suiviprojet.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.toubkalit.suiviprojet.dto.auth.ChangePasswordRequest;
import ma.toubkalit.suiviprojet.dto.auth.LoginRequest;
import ma.toubkalit.suiviprojet.dto.employe.EmployeResponseDto;
import ma.toubkalit.suiviprojet.services.EmployeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        if (request.getLogin() == null || request.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ma.toubkalit.suiviprojet.exceptions.ApiError(400, "Login ou mot de passe manquant"));
        }

        try {
            UserDetails user = userDetailsService.loadUserByUsername(request.getLogin());

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ma.toubkalit.suiviprojet.exceptions.ApiError(401, "Identifiant ou mot de passe incorrect"));
            }

            String token = jwtService.generateToken(user.getUsername());
            return ResponseEntity.ok(token);

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ma.toubkalit.suiviprojet.exceptions.ApiError(401, "Identifiant ou mot de passe incorrect"));
        }
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