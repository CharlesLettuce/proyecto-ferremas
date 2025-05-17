package com.ferremas.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger; // Asegúrate de importar Logger
import org.slf4j.LoggerFactory; // Asegúrate de importar LoggerFactory
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

// LA LÍNEA .addFilterBefore NO VA AQUÍ

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(
        JwtAuthenticationFilter.class
    ); // AÑADE ESTO

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // LOG AÑADIDO
        logger.debug(
            "Procesando request para URI: {}",
            request.getRequestURI()
        );

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // LOG MODIFICADO/AÑADIDO
            logger.warn(
                "No hay cabecera Authorization con Bearer token para URI: {}. Dejando pasar.",
                request.getRequestURI()
            );
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        try {
            username = jwtUtil.extractUsername(jwt);
            // LOG AÑADIDO
            logger.info(
                "Username extraído del token: {} para URI: {}",
                username,
                request.getRequestURI()
            );
        } catch (Exception e) {
            // LOG AÑADIDO
            logger.error(
                "Error al extraer username del token JWT: {}. Token: {}",
                e.getMessage(),
                jwt
            );
            filterChain.doFilter(request, response);
            return;
        }

        if (
            username != null &&
            SecurityContextHolder.getContext().getAuthentication() == null
        ) {
            // LOG AÑADIDO
            logger.info(
                "SecurityContext es null, cargando UserDetails para {}",
                username
            );
            UserDetails userDetails = null;
            try {
                userDetails = this.userDetailsService.loadUserByUsername(
                        username
                    );
                logger.info(
                    "UserDetails cargados: Username: {}, Authorities: {}",
                    userDetails.getUsername(),
                    userDetails.getAuthorities()
                );
            } catch (Exception e) {
                logger.error(
                    "Error al cargar UserDetails para {}: {}",
                    username,
                    e.getMessage()
                );
                filterChain.doFilter(request, response); // Importante si no se puede cargar el usuario
                return;
            }

            if (jwtUtil.validateToken(jwt, userDetails)) {
                // LOG AÑADIDO
                logger.info(
                    "Token válido para {}. Estableciendo autenticación.",
                    username
                );
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );
                authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                // LOG AÑADIDO
                logger.warn("Token inválido para {}.", username);
            }
        } else {
            // LOG AÑADIDO
            logger.info(
                "Username es null o SecurityContext ya tiene autenticación. Username: {}, Auth: {}",
                username,
                SecurityContextHolder.getContext().getAuthentication()
            );
        }
        filterChain.doFilter(request, response);
    }
}
