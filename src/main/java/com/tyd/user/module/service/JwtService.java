package com.tyd.user.module.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "15B85F8476A1B5CBF7BA2CA0B3B2690B9BE7CB57A9D261B99439DD1363B3215E";

    public String extractUserEmail(String jwToken) {
        return extractClaim(jwToken, Claims::getSubject);
    }

    public <T> T extractClaim(String jwToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwToken);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwToken) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jwToken)
                .getBody();

    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaim, UserDetails userDetail) {
        return Jwts
                .builder()
                .setClaims(extraClaim)
                .setSubject(userDetail.getUsername())
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .compact();
    }

    public boolean isTokenValid(String jwToken, UserDetails userDetails){
        final String userName = extractUserEmail(jwToken);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(jwToken);
    }

    private boolean isTokenExpired(String jwToken) {
        return extractExpirationDate(jwToken).before(new Date());
    }

    private Date extractExpirationDate(String jwToken) {
        return extractClaim(jwToken,Claims::getExpiration);
    }
}
