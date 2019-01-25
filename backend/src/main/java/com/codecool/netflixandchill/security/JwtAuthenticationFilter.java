package com.codecool.netflixandchill.security;

import com.codecool.netflixandchill.dto.ErrorDTO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

import static com.codecool.netflixandchill.security.SecurityConstants.*;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    private JwtAuthenticationSuccessHandler successHandler;

    private JwtAuthenticationFailureHandler failureHandler;

    JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                            JwtAuthenticationSuccessHandler successHandler,
                            JwtAuthenticationFailureHandler failureHandler) {
        this.authenticationManager = authenticationManager;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.setFilterProcessesUrl(LOGIN_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            JsonObject jsonObject = (JsonObject) new JsonParser().parse(
                    request.getReader()
                            .lines()
                            .collect(Collectors.joining(System.lineSeparator())));

            String username = jsonObject.get("username").getAsString();
            String password = jsonObject.get("password").getAsString();

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>())
            );
        } catch (IOException e) {
            logger.info("Exception during parsing Json!");
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        logger.info("Successful authentication!");
        Claims claims = Jwts.claims()
                .setSubject(((User) authResult.getPrincipal()).getUsername());

        String token = Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        response.addHeader("Access-Control-Expose-Headers", HEADER_STRING);

        this.successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        logger.info("Failed authentication!");

        response.getWriter().write(new Gson().toJson(ErrorDTO.builder()
                .error("invalid parameters")
                .message("Username or password is incorrect!")
                .build()));
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);

        this.failureHandler.onAuthenticationFailure(request, response, failed);
    }

}
