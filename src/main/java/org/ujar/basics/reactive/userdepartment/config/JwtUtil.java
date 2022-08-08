package org.ujar.basics.reactive.userdepartment.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.ujar.basics.reactive.userdepartment.entity.User;

@Component
@RequiredArgsConstructor
public class JwtUtil {

  private final JwtTokenProperties jwtTokenProperties;

  public String extractUsername(String authToken) {
    return getClaimsFromToken(authToken)
        .getSubject();
  }

  public Claims getClaimsFromToken(String authToken) {
    String key = Base64.getEncoder().encodeToString(jwtTokenProperties.secret().getBytes(StandardCharsets.UTF_8));
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(authToken)
        .getBody();
  }

  public boolean validateToken(String authToken) {
    return getClaimsFromToken(authToken)
        .getExpiration()
        .after(new Date());
  }

  public String generateToken(User user) {
    HashMap<String, Object> claims = new HashMap<>();
    claims.put("role", List.of(user.getRole()));

    long expirationSeconds = jwtTokenProperties.expiration();
    Date creationDate = new Date();
    Date expirationDate = new Date(creationDate.getTime() + expirationSeconds * 1000);

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(user.getName())
        .setIssuedAt(creationDate)
        .setExpiration(expirationDate)
        .signWith(Keys.hmacShaKeyFor(jwtTokenProperties.secret().getBytes(StandardCharsets.UTF_8)))
        .compact();
  }
}
