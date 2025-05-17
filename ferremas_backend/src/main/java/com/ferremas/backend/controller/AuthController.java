package com.ferremas.backend.controller;

import com.ferremas.backend.dto.AuthRequest;
import com.ferremas.backend.dto.AuthResponse;
import com.ferremas.backend.dto.RegisterRequest;
import com.ferremas.backend.model.Usuario;
import com.ferremas.backend.repository.UsuarioRepository;
import com.ferremas.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/api/auth/register")
    public ResponseEntity<?> registerUser(
        @RequestBody RegisterRequest registerRequest
    ) {
        if (usuarioRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                "Error: El nombre de usuario ya está en uso."
            );
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(registerRequest.getUsername());
        usuario.setPassword(
            passwordEncoder.encode(registerRequest.getPassword())
        );
        usuario.setRole(
            registerRequest.getRole() != null
                ? registerRequest.getRole()
                : "ROLE_USER"
        );

        usuarioRepository.save(usuario);
        return ResponseEntity.ok("Usuario registrado exitosamente.");
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<?> createAuthenticationToken(
        @RequestBody AuthRequest authRequest
    ) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(),
                    authRequest.getPassword()
                )
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                "Credenciales incorrectas."
            );
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(
            authRequest.getUsername()
        );
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginWithQueryParams(
        @RequestParam("user") String username,
        @RequestParam("encryptedPass") String password
    ) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                "Error: Usuario o contraseña incorrectos."
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                "Error durante la autenticación: " + e.getMessage()
            );
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(
            username
        );
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}
