package com.example.fullstackboard.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

// 토큰 발급/검증
@Component
public class JwtTokenProvider {

    // 실제 서비스에서는 환경변수/secret 파일에서 주입받아야 함
    private final Key secretKey = Keys.hmacShaKeyFor("MySecretKeyMySecretKeyMySecretKey1234".getBytes());

    private final long validityInMs = 1000L * 60 * 60;  // 1시간

    // 토큰 생성
    public String createToken(String email, String role) {
        Claims claims = Jwts.claims().setSubject(email);  // subject에 email 저장
        claims.put("role", role);  // role 정보도 저장

        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMs);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey, SignatureAlgorithm.HS256)  // HMAC-SHA256 방식 서명
                .compact();
    }

    // 토큰에서 email 꺼내기
    public String getEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

}
