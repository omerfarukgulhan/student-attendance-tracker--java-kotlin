package com.ofg.attendance.security.token;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

import com.ofg.attendance.model.entity.User;
import com.ofg.attendance.model.request.SignInCredentials;
import com.ofg.attendance.model.response.UserResponse;
import com.ofg.attendance.service.abstracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
@ConditionalOnProperty(name = "app.token-type", havingValue = "jwt")
public class JwtTokenService implements TokenService {
    private static final String BEARER = "Bearer";

    private final UserService userService;
    private final ObjectMapper mapper;
    private final String secretKey;
    private final long expirationTime;
    private final String issuer;
    private final String audience;

    @Autowired
    public JwtTokenService(UserService userService, ObjectMapper mapper,
                           @Value("${app.jwt.secret-key}") String secretKey,
                           @Value("${app.jwt.expiration-time}") long expirationTime,
                           @Value("${app.jwt.issuer}") String issuer,
                           @Value("${app.jwt.audience}") String audience) {
        this.userService = userService;
        this.mapper = mapper;
        this.secretKey = secretKey;
        this.expirationTime = expirationTime;
        this.issuer = issuer;
        this.audience = audience;
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    @Override
    public Token createToken(User user, SignInCredentials signInCredentials) {
        try {
            UserResponse userResponse = new UserResponse(user);
            String subject = mapper.writeValueAsString(userResponse);
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);
            Date expiryDate = new Date(nowMillis + expirationTime * 1000);
            String token = Jwts.builder()
                    .setSubject(subject)
                    .setIssuer(issuer)
                    .setAudience(audience)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(getKey())
                    .compact();
            return new Token(BEARER, token);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error creating token", e);
        }
    }

    @Override
    public Optional<User> verifyToken(String authorizationHeader) {
        return extractToken(authorizationHeader).flatMap(token -> {
            JwtParser parser = Jwts.parserBuilder().setSigningKey(getKey()).build();
            try {
                Jws<Claims> claims = parser.parseClaimsJws(token);
                String subject = claims.getBody().getSubject();
                UserResponse userResponse = mapper.readValue(subject, UserResponse.class);
                return Optional.ofNullable(userService.getUserByEmail(userResponse.email()));
            } catch (JwtException | JsonProcessingException e) {
                return Optional.empty();
            }
        });
    }

    private Optional<String> extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER + " ")) {
            return Optional.of(authorizationHeader.substring(BEARER.length() + 1));
        }
        return Optional.empty();
    }
}