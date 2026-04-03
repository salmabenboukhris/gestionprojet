package ma.toubkalit.suiviprojet.security;

import lombok.RequiredArgsConstructor;
import ma.toubkalit.suiviprojet.security.JwtService;
import ma.toubkalit.suiviprojet.security.CustomUserDetailsService;
import ma.toubkalit.suiviprojet.dto.auth.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {

        if (request.getLogin() == null || request.getPassword() == null) {
            throw new RuntimeException("Login ou mot de passe manquant");
        }

        UserDetails user = userDetailsService.loadUserByUsername(request.getLogin());

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        String token = jwtService.generateToken(user.getUsername());

        return ResponseEntity.ok(token);
    }}