package com.autum.examapp.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        String path = request.getServletPath();

        /* STATIC FILES */
        if (path.equals("/") ||
                path.equals("/index.html") ||
                path.endsWith(".js") ||
                path.endsWith(".css") ||
                path.endsWith(".ico")) {

            filterChain.doFilter(request, response);
            return;
        }

        /* PUBLIC APIs */
        if (path.startsWith("/users/login") ||
                path.startsWith("/users/save") ||
                path.startsWith("/users/verify-otp") ||
                path.startsWith("/users/resend-otp") ||
                path.startsWith("/users/forgot-password") ||
                path.startsWith("/users/reset-password") ||
        path.startsWith( "/users/verify-reset-otp"))
        {

            filterChain.doFilter(request, response);
            return;
        }
        if(header!=null && header.startsWith("Bearer ")){

            String token = header.substring(7);

            Claims claims = jwtUtil.extractClaims(token);

            String email = claims.getSubject();

            String role = claims.get("role", String.class);

            if(role == null){
                role = "ROLE_USER";
            }

            System.out.println("ROLE FROM TOKEN: " + role);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            List.of(new SimpleGrantedAuthority(role))
                    );

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request,response);
    }
}