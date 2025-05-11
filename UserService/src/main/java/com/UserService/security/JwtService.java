package com.UserService.security;

import org.springframework.stereotype.Service;

import com.UserService.model.User;

import io.jsonwebtoken.*;
import java.security.Key;
import java.util.Date;
@Service
public class JwtService {
	 private static final long EXPIRATION = 1000 * 60 * 60 * 24; // 24 hours
	    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	    public String generateToken(User user) {
	        return Jwts.builder()
	                .setSubject(user.getEmail())
	                .claim("role", user.getRole())
	                .setIssuedAt(new Date())
	                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
	                .signWith(key)
	                .compact();
	    }

	    public String extractUsername(String token) {
	        return Jwts.parserBuilder()
	                .setSigningKey(key)
	                .build()
	                .parseClaimsJws(token)
	                .getBody()
	                .getSubject();
	    }

	    public boolean isTokenValid(String token, User user) {
	        final String username = extractUsername(token);
	        return username.equals(user.getEmail()) && !isTokenExpired(token);
	    }

	    private boolean isTokenExpired(String token) {
	        Date expiration = Jwts.parserBuilder()
	                .setSigningKey(key)
	                .build()
	                .parseClaimsJws(token)
	                .getBody()
	                .getExpiration();
	        return expiration.before(new Date());
	    }

}
