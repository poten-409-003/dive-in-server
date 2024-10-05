package com.poten.dive_in.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poten.dive_in.common.dto.CommonResponse;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper = new ObjectMapper(); // ObjectMapper 선언 및 초기화

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Request에서 JWT 토큰을 추출
        String token = getJwtFromRequest(request,"Authorization");

        try {
            if (token != null) {

                if(token.startsWith("Bearer ")){
                    token = token.substring(7);
                }

                if (jwtTokenProvider.validateToken(token)) {

                    String email = jwtTokenProvider.getEmailFromToken(token);
                    var authorities = Collections.singletonList(new SimpleGrantedAuthority(jwtTokenProvider.getRoleFromToken(token).name()));
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(email, null, authorities);

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                } else {
                    // 토큰이 만료된 경우
                    String refreshToken = getJwtFromRequest(request, "X-Refresh-Token");
                    if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
                        // 새로운 Access Token 생성
                        String newAccessToken = jwtTokenProvider.createAccessToken(
                                jwtTokenProvider.getMemberIdFromToken(refreshToken),
                                jwtTokenProvider.getEmailFromToken(refreshToken),
                                jwtTokenProvider.getRoleFromToken(refreshToken)
                        );

                        // 새 Access Token을 응답 헤더에 추가
                        response.setHeader("Authorization", "Bearer " + newAccessToken); // Authorization 헤더에 추가

                        setAuthenticationContext(newAccessToken, request);


                    } else {
                        // RefreshToken이 유효하지 않으면 에러 처리
                        sendErrorResponse(response, "Invalid or expired refresh token", HttpServletResponse.SC_FORBIDDEN);
                        return;
                    }
                }
            }
        } catch (JwtException e) {
            // Access Token이 변조되었거나 유효하지 않은 경우 에러 처리
            sendErrorResponse(response, "Invalid or tampered access token", HttpServletResponse.SC_FORBIDDEN);
            return; // 필터 체인 중단
        }

        // 필터 체인을 계속해서 진행
        filterChain.doFilter(request, response);
    }

    // Request에서 JWT 토큰을 추출하는 메서드
    private String getJwtFromRequest(HttpServletRequest request, String tokenName) {
        return request.getHeader(tokenName);
    }

    // 에러 응답을 클라이언트에게 전송하는 메서드
    private void sendErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");

        CommonResponse<Object> errorResponse = CommonResponse.error(message); // CommonResponse 사용
        String jsonResponse = objectMapper.writeValueAsString(errorResponse); // JSON 변환
        response.getWriter().write(jsonResponse);
    }

    // 인증 컨텍스트 설정 메서드
    private void setAuthenticationContext(String token, HttpServletRequest request) {
        String email = jwtTokenProvider.getEmailFromToken(token);
        var authorities = Collections.singletonList(new SimpleGrantedAuthority(jwtTokenProvider.getRoleFromToken(token).name()));
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(email, null, authorities);

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }



}
