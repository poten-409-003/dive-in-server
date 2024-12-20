package com.poten.dive_in.auth.jwt;

import com.poten.dive_in.auth.enums.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class JwtTokenProvider {

    private final Key secretKey;

    public JwtTokenProvider(@Value("${jwt.secret_key}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // Access Token 생성
    public String createAccessToken(Long memberId, String email, Role role) {
        return createToken(memberId, email, role, 1L);
    }

    // Refresh Token 생성
    public String createRefreshToken(Long memberId, String email, Role role) {
        return createToken(memberId, email, role, 10080L); //7일
    }

    private String createToken(Long memberId, String email, Role role, long validityInMinutes) {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiration_date = now.plusMinutes(validityInMinutes);

        Claims claims = Jwts.claims().setSubject(memberId.toString());
        claims.put("email", email);

        if (role != null) {
            claims.put("role", role); // 권한 정보 추가
        }

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(convertToDateFromLocalDateTime(now))
                .setExpiration(convertToDateFromLocalDateTime(expiration_date))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }


    //JWT 토큰에서 member_id 추출
    public Long getMemberIdFromToken(String token) {
        String memberIdString = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return Long.parseLong(memberIdString);
    }

    // JWT 토큰에서 Role 추출
    public Role getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Role.valueOf((String) claims.get("role"));
    }

    // JWT 토큰에서 email 추출
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return (String) claims.get("email");
    }


    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (ExpiredJwtException e) {
            return false; // 토큰이 만료된 경우
        } catch (JwtException | IllegalArgumentException e) {
            return false; // 변조되거나 유효하지 않은 토큰인 경우
        }
    }


    // 토큰에서 만료 기간 추출
    public LocalDateTime getExpirationFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Date expiration_date = claims.getExpiration();
        return LocalDateTime.ofInstant(expiration_date.toInstant(), ZoneId.systemDefault());
    }


    // LocalDateTime -> Date 변환
    private Date convertToDateFromLocalDateTime(LocalDateTime dateToConvert) {
        return Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
    }

}
