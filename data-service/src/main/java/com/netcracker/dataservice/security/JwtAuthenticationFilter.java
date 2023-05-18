package com.netcracker.dataservice.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    //    private AuthenticationManager authenticationManager;
//
//
//    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//    }
//
//    //Вызывается когда мы отправляем POST запрос to /login
//    /*
//    мы передаем {"username":"ilya","password":"ilya123"} в теле запроса
//    * */
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        AuthenticationForm credentials= null;
//        try {
//            credentials = new ObjectMapper().readValue(request.getInputStream(), AuthenticationForm.class);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        //Создается токен логина
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//                credentials.getUsername(),
//                credentials.getPassword(),
//                new ArrayList<>());
//
//        Authentication auth = authenticationManager.authenticate(authenticationToken);
//        return auth;
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request,
//                                            HttpServletResponse response,
//                                            FilterChain chain,
//                                            Authentication authResult) throws IOException, ServletException {
//        //получаем пользователя по результатам проверки
//        UserPrincipal principal = (UserPrincipal) authResult.getPrincipal();
//
//        //Создаем JWT Token
//        String token = JWT.create()
//                .withSubject(principal.getUsername())
//                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
//                .withIssuer(request.getRequestURL().toString())
//                .withClaim("roles", String.valueOf(principal.getAuthorities()))
//                .sign(HMAC512(JwtProperties.SECRET.getBytes()));
//
//
//        //добавляем токен в Header ответа
//        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);
//        Map<String, String> tokens = new HashMap<>();
//        tokens.put(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);
//        response.setContentType(APPLICATION_JSON_VALUE);
//        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
//    }
    private final JwtService jwtService;
    private final UserDetailsService userPrincipalDetailsService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String customerEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        customerEmail = jwtService.extractUsername(jwt);
        if (customerEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails customerDetails = this.userPrincipalDetailsService.loadUserByUsername(customerEmail);
            if(jwtService.isTokenValid(jwt,customerDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        customerDetails,
                        null,
                        customerDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            filterChain.doFilter(request,response);
        }
    }
}
