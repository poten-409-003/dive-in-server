package com.poten.dive_in.auth.service;

import com.poten.dive_in.auth.enums.SocialType;
import com.poten.dive_in.auth.jwt.JwtTokenProvider;
import com.poten.dive_in.auth.entity.Member;
import com.poten.dive_in.auth.entity.TokenManager;
import com.poten.dive_in.auth.enums.Role;
import com.poten.dive_in.auth.repository.MemberRepository;
import com.poten.dive_in.auth.repository.TokenManagerRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenManagerRepository tokenManagerRepository;
    private final HttpServletResponse httpServletResponse;


    public void login(String email,String nickName, HttpServletResponse response){

        // 이메일로 회원 조회
        Member member = memberRepository.findByEmail(email).orElseGet(() -> {

            // 이메일이 없을 경우 새로운 Member 생성
            Member newMember = Member.builder()
                    .email(email)
                    .role(Role.ROLE_USER) // 디폴트 USER
                    .nickName(nickName)
                    .socialType(SocialType.KAKAO)
                    .build();
            return memberRepository.save(newMember);
        });

        // AccessToken 및 Refresh Token 발급
        String accessToken = jwtTokenProvider.createAccessToken(member.getId(),member.getEmail(), member.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId(),member.getEmail(),member.getRole());

        // 쿠키 설정
        setCookies(accessToken,refreshToken);

        // refreshToken 만료 기한
        LocalDateTime refreshTokenExpirationDate = jwtTokenProvider.getExpirationFromToken(refreshToken);

        // TokenManager 빌더 사용하여 생성
        TokenManager tokenManager = TokenManager.builder()
                .expirationDate(refreshTokenExpirationDate)
                .member(member)
                .refreshToken(refreshToken)
                .build();

        // TokenManager 저장
        tokenManagerRepository.save(tokenManager);

    }

    @Transactional
    public void logOut(HttpServletRequest request, HttpServletResponse response) {
        // 쿠키에서 refresh_token 추출
        String token = getJwtFromRequest(request, "refresh_token");

        if (token != null) {
            tokenManagerRepository.deleteByRefreshToken(token);

            // 클라이언트의 쿠키 삭제
            deleteCookies(response);
        }
    }

    public void setCookies(String accessToken, String refreshToken){
        httpServletResponse.addHeader("Set-Cookie","access_token="+accessToken
                +"; Path=/; HttpOnly; Secure; SameSite=None; ");

        httpServletResponse.addHeader("Set-Cookie","refresh_token="+refreshToken
        +"; Path=/; HttpOnly; Secure; SameSite=None; ");
    }

    public void deleteCookies(HttpServletResponse response) {
// Access Token 쿠키 삭제
        response.addHeader("Set-Cookie", "access_token=; Path=/; HttpOnly; Secure; SameSite=None; Max-Age=0;");

        // Refresh Token 쿠키 삭제
        response.addHeader("Set-Cookie", "refresh_token=; Path=/; HttpOnly; Secure; SameSite=None; Max-Age=0;");

    }


    private String getJwtFromRequest(HttpServletRequest request, String token_name) {
        // 쿠키에서 JWT 토큰을 찾기
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(token_name)) { // Access Token 쿠키에서 가져오기
                    return cookie.getValue(); // 쿠키 값 반환
                }
            }
        }
        return null;
    }
}
