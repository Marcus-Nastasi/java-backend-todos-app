package com.app.todos.Security;

import com.app.todos.Repository.User.UserRepo;
import com.app.todos.Services.Auth.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepo userRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = recover(request);

        if (token != null) {
            String email = tokenService.validate(token);
            UserDetails u = userRepo.findByEmail(email);

            var auth = new UsernamePasswordAuthenticationToken(u, null, u.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }

    private String recover(HttpServletRequest request) {
        var headers = request.getHeader("Authorization");
        if (headers == null) return null;
        return headers.replace("Bearer ", "");
    }
}






